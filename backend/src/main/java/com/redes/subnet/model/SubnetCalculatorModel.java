package com.redes.subnet.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubnetCalculatorModel {
    private String baseNetwork;
    private int requiredHosts;
    private int subnetNumber;
    private String resultNetwork;
    private String binaryRepresentation;
    private String firstUsableAddress;
    private String lastUsableAddress;
    private String defaultGateway;
    private String broadcast;
    private long totalUsableHosts;
    private int newMask;
    private BinaryVisualization binaryVisualization;

    @Data
    public static class BinaryVisualization {
        private String[] octets;
        private int baseMask;
        private int newMask;
        private String[] colors; // "red", "green", "blue" para cada bit

        public BinaryVisualization(int size) {
            this.octets = new String[4];
            this.colors = new String[32];
        }
    }
} 