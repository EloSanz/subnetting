package com.redes.subnet.service;

import com.redes.subnet.model.SubnetCalculatorModel;
import com.redes.subnet.model.SubnetCalculatorModel.BinaryVisualization;
import org.springframework.stereotype.Service;

@Service
public class SubnetCalculatorService {

    public SubnetCalculatorModel calculateSubnet(String baseNetwork, int requiredHosts, int subnetNumber) {
        validateParametersWithDetail(baseNetwork, requiredHosts, subnetNumber); // Validar primero
        try {
            SubnetCalculatorModel result = new SubnetCalculatorModel();
            result.setBaseNetwork(baseNetwork);
            result.setRequiredHosts(requiredHosts);
            result.setSubnetNumber(subnetNumber);

            // Separar la IP y la máscara
            String[] parts = baseNetwork.split("/");
            String ipAddress = parts[0];
            int baseMask = Integer.parseInt(parts[1]);
            
            // Calcular bits necesarios para hosts
            int hostBits = (int) Math.ceil(Math.log(requiredHosts + 2) / Math.log(2));
            int newMask = 32 - hostBits;
            result.setNewMask(newMask);
            
            // Convertir número de subred a binario
            String binarySubnet = String.format("%" + (newMask - baseMask) + "s", 
                Integer.toBinaryString(subnetNumber - 1))
                .replace(' ', '0');
            result.setBinaryRepresentation(binarySubnet);
            
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
            
            int binaryIndex = 0;
            String[] visualOctets = new String[4];
            String[] colors = new String[32];
            
            for (int octet = 0; octet < 4; octet++) {
                StringBuilder octetBits = new StringBuilder();
                for (int bit = 0; bit < 8; bit++) {
                    int globalBit = (octet * 8) + bit + 1;
                    if (globalBit <= baseMask) {
                        octetBits.append("1");
                        colors[globalBit-1] = "red";
                    } else if (globalBit <= newMask) {
                        if (binaryIndex < binarySubnet.length() && 
                            binarySubnet.charAt(binaryIndex) == '1') {
                            octetBits.append("1");
                        } else {
                            octetBits.append("0");
                        }
                        colors[globalBit-1] = "green";
                        binaryIndex++;
                    } else {
                        octetBits.append("h");
                        colors[globalBit-1] = "blue";
                    }
                }
                visualOctets[octet] = octetBits.toString();
            }
            
            visualization.setOctets(visualOctets);
            visualization.setColors(colors);
            result.setBinaryVisualization(visualization);
            
            // Calcular direcciones
            binaryIndex = 0;
            for (int octet = 2; octet < 4; octet++) {
                int octetValue = 0;
                for (int bit = 0; bit < 8; bit++) {
                    int globalBit = (octet * 8) + bit + 1;
                    if (globalBit > baseMask && globalBit <= newMask) {
                        if (binaryIndex < binarySubnet.length() && 
                            binarySubnet.charAt(binaryIndex) == '1') {
                            octetValue += Math.pow(2, 7-bit);
                        }
                        binaryIndex++;
                    } else if (globalBit <= baseMask && ipOctets[octet] >= 128) {
                        octetValue += Math.pow(2, 7-bit);
                    }
                }
                ipOctets[octet] = octetValue;
            }
            
            // Construir resultados
            String resultIP = String.format("%d.%d.%d.%d", 
                ipOctets[0], ipOctets[1], ipOctets[2], ipOctets[3]);
            result.setResultNetwork(resultIP + "/" + newMask);
            
            long subnetSize = (long) Math.pow(2, 32 - newMask);
            result.setTotalUsableHosts(subnetSize);
            
            result.setFirstUsableAddress(String.format("%d.%d.%d.%d",
                ipOctets[0], ipOctets[1], ipOctets[2], ipOctets[3] + 1));
                
            result.setLastUsableAddress(String.format("%d.%d.%d.%d",
                ipOctets[0], ipOctets[1], ipOctets[2], ipOctets[3] + (int)subnetSize - 2));
                
            result.setDefaultGateway(String.format("%d.%d.%d.%d",
                ipOctets[0], ipOctets[1], ipOctets[2], ipOctets[3] + (int)subnetSize - 2));
                
            result.setBroadcast(String.format("%d.%d.%d.%d",
                ipOctets[0], ipOctets[1], ipOctets[2], ipOctets[3] + (int)subnetSize - 1));
            
            return result;
            
        } catch (Exception e) {
            throw new RuntimeException("Error calculando la subred: " + e.getMessage());
        }
    }
    
    public void validateParametersWithDetail(String baseNetwork, int requiredHosts, int subnetNumber) {
        try {
            String[] parts = baseNetwork.split("/");
            int baseMask = Integer.parseInt(parts[1]);
            
            // Calcular bits necesarios para hosts
            int hostBits = (int) Math.ceil(Math.log(requiredHosts + 2) / Math.log(2));
            int newMask = 32 - hostBits;

            if (newMask <= baseMask) {
                throw new IllegalArgumentException(
                    String.format("La cantidad de hosts requeridos (%d) necesita %d bits, " +
                    "pero con la máscara base /%d solo tienes %d bits disponibles para hosts y subredes. " +
                    "Reduce la cantidad de hosts requeridos o usa una red base con más bits disponibles.",
                    requiredHosts, hostBits, baseMask, 32 - baseMask));
            }
            
            int bitsParaSubredes = newMask - baseMask;
            int maxSubredes = (int) Math.pow(2, bitsParaSubredes);
            
            if (subnetNumber > maxSubredes) {
                throw new IllegalArgumentException(
                    String.format("El número de subred %d no es válido. Con la red base /%d y " +
                    "los hosts requeridos (%d), tienes %d bits para subredes, " +
                    "lo que permite un máximo de %d subredes (numeradas de 1 a %d).",
                    subnetNumber, baseMask, requiredHosts, bitsParaSubredes, maxSubredes, maxSubredes));
            }
            
        } catch (IllegalArgumentException e) {
            throw e; // Reenviar errores de validación con mensaje detallado
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Error al validar los parámetros. Asegúrate de que la red base tenga el formato correcto (ej: 192.168.1.0/24)");
        }
    }
    
    public boolean validateParameters(String baseNetwork, int requiredHosts, int subnetNumber) {
        try {
            validateParametersWithDetail(baseNetwork, requiredHosts, subnetNumber);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
} 