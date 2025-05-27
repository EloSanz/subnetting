import { useState } from 'react';
import { SubnetResult, SubnetFormData } from '../types/subnet.types';
import { calculateSubnet } from '../services/subnetService';

export const useSubnetCalculator = () => {
    const [formData, setFormData] = useState<SubnetFormData>({
        ipAddress: '',
        maskBits: '',
        requiredHosts: '',
        subnetNumber: '',
        subnettingBits: '',
        useSubnettingBits: false
    });
    const [result, setResult] = useState<SubnetResult | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [isDarkTheme, setIsDarkTheme] = useState(false);

    const validateIpFormat = (ip: string): string | null => {
        const parts = ip.split('.');
        if (parts.length !== 4) {
            return 'La dirección IP debe tener exactamente 4 octetos separados por puntos';
        }

        for (const part of parts) {
            if (!/^\d+$/.test(part)) {
                return 'Los octetos solo pueden contener números';
            }
            
            const num = parseInt(part);
            if (isNaN(num) || num < 0 || num > 255) {
                return 'Cada octeto debe ser un número entre 0 y 255';
            }
        }

        return null;
    };

    const validateMask = (mask: string): string | null => {
        const maskNum = parseInt(mask);
        if (isNaN(maskNum) || maskNum < 0 || maskNum > 32) {
            return 'La máscara debe ser un número entre 0 y 32';
        }
        return null;
    };

    const handleInputChange = (field: keyof SubnetFormData, value: string | boolean) => {
        if (field === 'useSubnettingBits') {
            setFormData(prev => ({
                ...prev,
                useSubnettingBits: value as boolean,
                requiredHosts: value ? '' : prev.requiredHosts,
                subnettingBits: value ? prev.subnettingBits : ''
            }));
        } else {
            setFormData(prev => ({
                ...prev,
                [field]: value
            }));
        }
    };

    const handleCalculate = async () => {
        try {
            setError(null);

            if (!formData.ipAddress) {
                setError('Por favor, ingresa una dirección IP');
                return;
            }

            if (!formData.maskBits) {
                setError('Por favor, ingresa la máscara de red');
                return;
            }

            const ipError = validateIpFormat(formData.ipAddress);
            if (ipError) {
                setError(ipError);
                return;
            }

            const maskError = validateMask(formData.maskBits);
            if (maskError) {
                setError(maskError);
                return;
            }

            if (!formData.useSubnettingBits && !formData.requiredHosts) {
                setError('Por favor, ingresa la cantidad de hosts requeridos');
                return;
            }

            if (formData.useSubnettingBits && !formData.subnettingBits) {
                setError('Por favor, ingresa la cantidad de bits para subnetting');
                return;
            }

            if (!formData.subnetNumber) {
                setError('Por favor, ingresa el número de subred');
                return;
            }

            const baseNetwork = `${formData.ipAddress}/${formData.maskBits}`;
            const result = await calculateSubnet(
                baseNetwork,
                formData.useSubnettingBits ? 2 : formData.requiredHosts as number,
                formData.subnetNumber as number,
                formData.useSubnettingBits,
                formData.useSubnettingBits ? formData.subnettingBits as number : undefined
            );
            
            setResult(result);
        } catch (e) {
            setError(e instanceof Error ? e.message : 'Error al conectar con el servidor');
            setResult(null);
        }
    };

    const handleClean = () => {
        setFormData(prev => ({
            ...prev,
            requiredHosts: '',
            subnetNumber: '',
            subnettingBits: '',
            useSubnettingBits: false
        }));
        setResult(null);
        setError(null);
    };

    const toggleTheme = () => {
        setIsDarkTheme(prev => !prev);
    };

    return {
        formData,
        result,
        error,
        isDarkTheme,
        handleInputChange,
        handleCalculate,
        handleClean,
        toggleTheme
    };
}; 