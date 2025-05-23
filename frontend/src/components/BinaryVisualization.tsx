import React from 'react';
import { Flex, Text, Box } from '@radix-ui/themes';

interface BinaryVisualizationProps {
    octets: string[];
    colors: string[];
    isDarkTheme?: boolean;
}

export const BinaryVisualization: React.FC<BinaryVisualizationProps> = ({ 
    octets, 
    colors,
    isDarkTheme = false 
}) => (
    <Flex direction="column" gap="2">
        <Text size="2" weight="bold" style={{ 
            color: isDarkTheme ? '#94a3b8' : 'inherit' 
        }}>
            Representación visual de los bits:
        </Text>
        <Box style={{ fontFamily: 'monospace', fontSize: '16px' }}>
            {octets.map((octet, i) => (
                <span key={i}>
                    {octet.split('').map((bit, j) => {
                        const colorIndex = i * 8 + j;
                        const color = colors[colorIndex] === 'red' ? '#ff6b6b' :
                                    colors[colorIndex] === 'green' ? '#51cf66' :
                                    '#339af0';
                        return (
                            <span key={j} style={{ color }}>
                                {bit}
                            </span>
                        );
                    })}
                    {i < 3 && '.'}
                </span>
            ))}
        </Box>
        <Flex gap="4">
            <Text size="1" style={{ color: '#ff6b6b' }}>■ Bits de red base (fijos)</Text>
            <Text size="1" style={{ color: '#51cf66' }}>■ Bits de subred (configurables)</Text>
            <Text size="1" style={{ color: '#339af0' }}>■ Bits de host</Text>
        </Flex>
    </Flex>
); 