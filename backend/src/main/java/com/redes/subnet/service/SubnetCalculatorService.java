package com.redes.subnet.service;

import com.redes.subnet.model.SubnetCalculatorModel;
import com.redes.subnet.model.SubnetCalculatorModel.BinaryVisualization;
import org.springframework.stereotype.Service;

@Service
public class SubnetCalculatorService {

    public SubnetCalculatorModel calculateSubnet(String baseNetwork, int requiredHosts, int subnetNumber) {
        return calculateSubnet(baseNetwork, requiredHosts, subnetNumber, false);
    }

    public SubnetCalculatorModel calculateSubnet(String baseNetwork, int value, int subnetNumber, boolean useSubnettingBits) {
        if (useSubnettingBits) {
            validateParametersWithDetail(baseNetwork, value, subnetNumber, true);
        } else {
            validateParametersWithDetail(baseNetwork, value, subnetNumber);
        }

        try {
            SubnetCalculatorModel result = new SubnetCalculatorModel();
            result.setBaseNetwork(baseNetwork);
            if (useSubnettingBits) {
                result.setSubnettingBits(value);
            } else {
                result.setRequiredHosts(value);
            }
            result.setSubnetNumber(subnetNumber);

            // Separar la IP y la máscara
            String[] parts = baseNetwork.split("/");
            String ipAddress = parts[0];
            int baseMask = Integer.parseInt(parts[1]);
            
            // Calcular bits necesarios para hosts o usar bits de subnetting directamente
            int newMask;
            if (useSubnettingBits) {
                newMask = baseMask + value;
                if (newMask > 32) {
                    throw new IllegalArgumentException("La suma de la máscara base y los bits de subnetting no puede exceder 32");
                }
            } else {
                int hostBits = (int) Math.ceil(Math.log(value + 2) / Math.log(2));
                newMask = 32 - hostBits;
            }
            result.setNewMask(newMask);
            
            // Crear visualización binaria
            BinaryVisualization visualization = new BinaryVisualization(32);
            visualization.setBaseMask(baseMask);
            visualization.setNewMask(newMask);
            
            // Separar octetos de la IP base
            String[] octets = ipAddress.split("\\.");
            int[] ipOctets = new int[4];
            for (int i = 0; i < 4; i++) {
                ipOctets[i] = Integer.parseInt(octets[i]);
            }
            
            // Calcular direcciones
            int[] newIpOctets = new int[4];
            System.arraycopy(ipOctets, 0, newIpOctets, 0, 4);

            // Convertir la dirección IP completa a un número de 32 bits
            long ipNumber = ((long)ipOctets[0] << 24) | 
                          ((long)ipOctets[1] << 16) | 
                          ((long)ipOctets[2] << 8) | 
                          ipOctets[3];

            // Crear máscara para preservar los bits de red base
            long baseNetworkMask = -1L << (32 - baseMask);
            long baseIp = ipNumber & baseNetworkMask;

            // Convertir el número de subred a su posición correcta
            // Restamos 1 porque las subredes empiezan en 1
            long subnetBits = (subnetNumber - 1L) << (32 - newMask);

            // Combinar todo
            long resultIp = baseIp | subnetBits;

            // Convertir el resultado de vuelta a octetos
            newIpOctets[0] = (int)(resultIp >> 24) & 0xFF;
            newIpOctets[1] = (int)(resultIp >> 16) & 0xFF;
            newIpOctets[2] = (int)(resultIp >> 8) & 0xFF;
            newIpOctets[3] = (int)resultIp & 0xFF;

            // Construir la representación binaria para visualización
            StringBuilder binaryRepresentation = new StringBuilder();
            int bitsDeSubnetting = newMask - baseMask;
            String formatoBinario = "%" + bitsDeSubnetting + "s";
            binaryRepresentation.append(
                String.format(formatoBinario, 
                    Integer.toBinaryString(subnetNumber - 1))
                    .replace(' ', '0')
            );
            result.setBinaryRepresentation(binaryRepresentation.toString());

            // Construir la visualización binaria completa
            String[] visualOctets = new String[4];
            String[] colors = new String[32];
            
            for (int octet = 0; octet < 4; octet++) {
                StringBuilder octetBits = new StringBuilder();
                for (int bit = 0; bit < 8; bit++) {
                    int globalBit = (octet * 8) + bit;
                    if (globalBit < baseMask) {
                        octetBits.append((resultIp >> (31 - globalBit)) & 1);
                        colors[globalBit] = "red";
                    } else if (globalBit < newMask) {
                        octetBits.append((subnetNumber - 1L >> (newMask - 1 - globalBit)) & 1);
                        colors[globalBit] = "green";
                    } else {
                        octetBits.append('h');
                        colors[globalBit] = "blue";
                    }
                }
                visualOctets[octet] = octetBits.toString();
            }
            
            visualization.setOctets(visualOctets);
            visualization.setColors(colors);
            result.setBinaryVisualization(visualization);
            
            // Construir resultados
            String resultIP = String.format("%d.%d.%d.%d", 
                newIpOctets[0], newIpOctets[1], newIpOctets[2], newIpOctets[3]);
            result.setResultNetwork(resultIP + "/" + newMask);
            
            long subnetSize = (long) Math.pow(2, 32 - newMask);
            result.setTotalUsableHosts(subnetSize);
            
            // Manejo especial para redes /30 y /31
            String networkType = "";
            String gatewayNote = "";
            
            if (newMask == 31) {
                networkType = "Red punto a punto (/31) - Optimizada para enlaces entre routers (RFC 3021)";
                gatewayNote = "N/A - En enlaces punto a punto /31 no se requiere gateway";
                // En /31 las dos direcciones son usables según RFC 3021
                result.setFirstUsableAddress(String.format("%d.%d.%d.%d",
                    newIpOctets[0], newIpOctets[1], newIpOctets[2], newIpOctets[3]));
                result.setLastUsableAddress(String.format("%d.%d.%d.%d",
                    newIpOctets[0], newIpOctets[1], newIpOctets[2], newIpOctets[3] + 1));
                result.setBroadcast("N/A - Red /31 no usa dirección de broadcast");
            } else if (newMask == 30) {
                networkType = "Red punto a punto (/30) - Típicamente usada para enlaces entre routers";
                gatewayNote = "N/A - En enlaces punto a punto /30 no se requiere gateway";
                result.setFirstUsableAddress(String.format("%d.%d.%d.%d",
                    newIpOctets[0], newIpOctets[1], newIpOctets[2], newIpOctets[3] + 1));
                result.setLastUsableAddress(String.format("%d.%d.%d.%d",
                    newIpOctets[0], newIpOctets[1], newIpOctets[2], newIpOctets[3] + 2));
                result.setBroadcast(String.format("%d.%d.%d.%d",
                    newIpOctets[0], newIpOctets[1], newIpOctets[2], newIpOctets[3] + 3));
            } else {
                result.setFirstUsableAddress(String.format("%d.%d.%d.%d",
                    newIpOctets[0], newIpOctets[1], newIpOctets[2], newIpOctets[3] + 1));
                result.setLastUsableAddress(String.format("%d.%d.%d.%d",
                    newIpOctets[0], newIpOctets[1], newIpOctets[2], newIpOctets[3] + (int)subnetSize - 2));
                result.setBroadcast(String.format("%d.%d.%d.%d",
                    newIpOctets[0], newIpOctets[1], newIpOctets[2], newIpOctets[3] + (int)subnetSize - 1));
                gatewayNote = String.format("%d.%d.%d.%d",
                    newIpOctets[0], newIpOctets[1], newIpOctets[2], newIpOctets[3] + (int)subnetSize - 2);
            }
            
            result.setDefaultGateway(gatewayNote);
            result.setNetworkType(networkType);
            
            return result;
            
        } catch (Exception e) {
            throw new RuntimeException("Error calculando la subred: " + e.getMessage());
        }
    }
    
    public void validateParametersWithDetail(String baseNetwork, int requiredHosts, int subnetNumber) {
        validateParametersWithDetail(baseNetwork, requiredHosts, subnetNumber, false);
    }

    public void validateParametersWithDetail(String baseNetwork, int value, int subnetNumber, boolean useSubnettingBits) {
        try {
            String[] parts = baseNetwork.split("/");
            if (parts.length != 2) {
                throw new IllegalArgumentException(
                    "El formato de red debe ser como: 192.168.1.0/24");
            }

            String[] octets = parts[0].split("\\.");
            if (octets.length != 4) {
                throw new IllegalArgumentException(
                    "La dirección IP debe tener exactamente 4 octetos");
            }

            for (String octet : octets) {
                int num = Integer.parseInt(octet);
                if (num < 0 || num > 255) {
                    throw new IllegalArgumentException(
                        "Cada octeto debe ser un número entre 0 y 255");
                }
            }

            int baseMask = Integer.parseInt(parts[1]);
            if (baseMask < 0 || baseMask > 32) {
                throw new IllegalArgumentException(
                    "La máscara debe ser un número entre 0 y 32");
            }
            
            // Calcular bits necesarios para hosts o usar bits de subnetting directamente
            int newMask;
            if (useSubnettingBits) {
                newMask = baseMask + value;
                if (newMask > 32) {
                    throw new IllegalArgumentException(
                        String.format("La suma de la máscara base /%d y los bits de subnetting (%d) no puede exceder 32",
                        baseMask, value));
                }
            } else {
                if (value < 2) {
                    throw new IllegalArgumentException(
                        "La cantidad de hosts requeridos debe ser al menos 2");
                }
                int hostBits = (int) Math.ceil(Math.log(value + 2) / Math.log(2));
                newMask = 32 - hostBits;

                if (newMask <= baseMask) {
                    throw new IllegalArgumentException(
                        String.format("La cantidad de hosts requeridos (%d) necesita %d bits, " +
                        "pero con la máscara base /%d solo tienes %d bits disponibles para hosts y subredes. " +
                        "Reduce la cantidad de hosts requeridos o usa una red base con más bits disponibles.",
                        value, hostBits, baseMask, 32 - baseMask));
                }
            }
            
            int bitsParaSubredes = newMask - baseMask;
            int maxSubredes = (int) Math.pow(2, bitsParaSubredes);
            
            if (subnetNumber > maxSubredes) {
                throw new IllegalArgumentException(
                    String.format("El número de subred %d no es válido. Con la red base /%d y " +
                    "%s (%d), tienes %d bits para subredes, " +
                    "lo que permite un máximo de %d subredes (numeradas de 1 a %d).",
                    subnetNumber, baseMask, 
                    useSubnettingBits ? "los bits de subnetting" : "los hosts requeridos",
                    value, bitsParaSubredes, maxSubredes, maxSubredes));
            }
            
        } catch (IllegalArgumentException e) {
            throw e; // Reenviar errores de validación con mensaje detallado
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Error al validar los parámetros. Asegúrate de que la red base tenga el formato correcto (ej: 192.168.1.0/24)");
        }
    }
    
    public boolean validateParameters(String baseNetwork, int value, int subnetNumber, boolean useSubnettingBits) {
        try {
            validateParametersWithDetail(baseNetwork, value, subnetNumber, useSubnettingBits);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateParameters(String baseNetwork, int requiredHosts, int subnetNumber) {
        return validateParameters(baseNetwork, requiredHosts, subnetNumber, false);
    }
} 