import { SubnetResult, ErrorResponse } from '../types/subnet.types';

const API_BASE_URL = 'http://localhost:8080/api';

export const calculateSubnet = async (
    baseNetwork: string,
    requiredHosts: number,
    subnetNumber: number
): Promise<SubnetResult> => {
    const response = await fetch(
        `${API_BASE_URL}/subnet/calculate?baseNetwork=${baseNetwork}&requiredHosts=${requiredHosts}&subnetNumber=${subnetNumber}`
    );
    
    const data = await response.json();
    
    if (!response.ok) {
        const errorData = data as ErrorResponse;
        throw new Error(`${errorData.message}: ${errorData.details}`);
    }
    
    return data as SubnetResult;
}; 