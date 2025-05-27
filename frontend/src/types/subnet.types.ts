export interface BinaryVisualization {
    octets: string[];
    colors: string[];
    baseMask: number;
    newMask: number;
}

export interface SubnetResult {
    baseNetwork: string;
    requiredHosts: number;
    subnetNumber: number;
    resultNetwork: string;
    binaryRepresentation: string;
    firstUsableAddress: string;
    lastUsableAddress: string;
    defaultGateway: string;
    broadcast: string;
    totalUsableHosts: number;
    newMask: number;
    subnettingBits?: number;
    networkType?: string;
    binaryVisualization?: BinaryVisualization;
}

export interface ErrorResponse {
    message: string;
    code: string;
    details: string;
}

export interface SubnetFormData {
    ipAddress: string;
    maskBits: string;
    requiredHosts: number | '';
    subnetNumber: number | '';
    subnettingBits: number | '';
    useSubnettingBits: boolean;
} 