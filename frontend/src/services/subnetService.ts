import { SubnetResult, ErrorResponse } from '../types/subnet.types';

const API_BASE_URL = 'http://localhost:8080/api';

export const calculateSubnet = async (
    baseNetwork: string,
    requiredHosts: number,
    subnetNumber: number,
    useSubnettingBits: boolean = false,
    subnettingBits?: number
): Promise<SubnetResult> => {
    let url = `${API_BASE_URL}/subnet/calculate?baseNetwork=${baseNetwork}&subnetNumber=${subnetNumber}&requiredHosts=${requiredHosts}`;
    
    if (useSubnettingBits && subnettingBits !== undefined) {
        url += `&subnettingBits=${subnettingBits}`;
    }
    
    const response = await fetch(url);
    
    const data = await response.json();
    
    if (!response.ok) {
        const errorData = data as ErrorResponse;
        throw new Error(`${errorData.message}: ${errorData.details}`);
    }
    
    return data as SubnetResult;
}; 