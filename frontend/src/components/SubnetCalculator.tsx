import React from 'react';
import { Theme, TextField, Button, Text, Card, Flex, Box, Container } from '@radix-ui/themes';
import '@radix-ui/themes/styles.css';
import { ErrorMessage } from './ErrorMessage';
import { BinaryVisualization } from './BinaryVisualization';
import { useSubnetCalculator } from '../hooks/useSubnetCalculator';

export default function SubnetCalculator() {
    const {
        formData,
        result,
        error,
        isDarkTheme,
        handleInputChange,
        handleCalculate,
        handleClean,
        toggleTheme
    } = useSubnetCalculator();

    return (
        <Theme appearance={isDarkTheme ? 'dark' : 'light'} accentColor="blue" grayColor="sand" scaling="95%">
            <Box style={{
                minHeight: '100vh',
                background: isDarkTheme ? 'linear-gradient(135deg, #1a1b1e 0%, #2f3133 100%)' : 'linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%)',
                padding: '2rem 1rem'
            }}>
                <Container size="3">
                    <Card style={{
                        maxWidth: 800,
                        margin: '0 auto',
                        padding: '2rem',
                        boxShadow: '0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1)',
                        borderRadius: '12px',
                        background: isDarkTheme ? '#1a1b1e' : 'white'
                    }}>
                        <Flex direction="column" gap="5">
                            <Flex align="center" justify="between">
                                <Flex align="center" gap="3">
                                    <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                        <rect x="4" y="4" width="16" height="16" rx="2" stroke="#339af0"/>
                                        <path d="M4 12h16" stroke="#51cf66"/>
                                        <path d="M12 4v16" stroke="#ff6b6b"/>
                                    </svg>
                                    <Text size="8" weight="bold" style={{ 
                                        color: isDarkTheme ? '#94a3b8' : 'inherit' 
                                    }}>Calculadora de Subredes</Text>
                                </Flex>
                                <Button 
                                    variant="soft" 
                                    onClick={toggleTheme}
                                    style={{ padding: '0 12px' }}
                                >
                                    {isDarkTheme ? '☀️' : '🌙'}
                                </Button>
                            </Flex>
                            
                            {error && (
                                <ErrorMessage 
                                    message={error} 
                                    onDismiss={() => handleInputChange('ipAddress', formData.ipAddress)}
                                />
                            )}

                            <Card style={{
                                background: '#f8f9fa',
                                padding: '1.5rem',
                                borderRadius: '8px'
                            }}>
                                <Flex direction="column" gap="3">
                                    <Text size="2" weight="bold" style={{ 
                                        color: isDarkTheme ? '#94a3b8' : 'inherit' 
                                    }}>Parámetros de entrada</Text>
                                    
                                    <Flex gap="1" align="center" style={{ maxWidth: '600px' }}>
                                        <Flex style={{ width: '350px' }}>
                                            <TextField.Root style={{ flex: 1 }}>
                                                <TextField.Input 
                                                    placeholder="Dirección IP (ej: 192.168.1.0)" 
                                                    value={formData.ipAddress}
                                                    onChange={(e) => handleInputChange('ipAddress', e.target.value)}
                                                />
                                            </TextField.Root>
                                            <Text size="2" style={{ margin: '0 4px', color: isDarkTheme ? '#94a3b8' : 'inherit' }}>/</Text>
                                            <TextField.Root style={{ width: '50px' }}>
                                                <TextField.Input 
                                                    placeholder="24"
                                                    value={formData.maskBits}
                                                    onChange={(e) => handleInputChange('maskBits', e.target.value)}
                                                    type="number"
                                                    min="0"
                                                    max="32"
                                                />
                                            </TextField.Root>
                                        </Flex>
                                        <Text size="2" style={{ 
                                            marginLeft: '8px', 
                                            color: isDarkTheme ? '#94a3b8' : '#666',
                                            width: '200px' 
                                        }}>Dirección de red base</Text>
                                    </Flex>
                                    
                                    <Flex gap="1" align="center" style={{ maxWidth: '600px' }}>
                                        <Flex style={{ width: '350px' }}>
                                            <TextField.Root style={{ width: '100%' }}>
                                                <TextField.Input 
                                                    type="number"
                                                    placeholder="Hosts requeridos"
                                                    value={formData.requiredHosts}
                                                    onChange={(e) => handleInputChange('requiredHosts', e.target.value)}
                                                    disabled={formData.useSubnettingBits}
                                                />
                                            </TextField.Root>
                                        </Flex>
                                        <Text size="2" style={{ 
                                            marginLeft: '8px', 
                                            color: isDarkTheme ? '#94a3b8' : '#666',
                                            width: '200px' 
                                        }}>Cantidad de hosts necesarios</Text>
                                    </Flex>

                                    <Flex gap="1" align="center" style={{ maxWidth: '600px', marginTop: '8px', marginBottom: '8px' }}>
                                        <Flex style={{ width: '350px', background: isDarkTheme ? '#2f3133' : '#e9ecef', padding: '8px', borderRadius: '4px' }}>
                                            <label style={{ 
                                                display: 'flex', 
                                                alignItems: 'center',
                                                marginRight: '16px',
                                                color: isDarkTheme ? '#94a3b8' : '#666',
                                                cursor: 'pointer',
                                                userSelect: 'none'
                                            }}>
                                                <input
                                                    type="checkbox"
                                                    checked={formData.useSubnettingBits}
                                                    onChange={(e) => handleInputChange('useSubnettingBits', e.target.checked)}
                                                    style={{ 
                                                        marginRight: '8px',
                                                        width: '16px',
                                                        height: '16px'
                                                    }}
                                                />
                                                Usar bits de subnetting
                                            </label>
                                        </Flex>
                                    </Flex>

                                    {formData.useSubnettingBits && (
                                        <Flex gap="1" align="center" style={{ maxWidth: '600px', marginTop: '4px' }}>
                                            <Flex style={{ width: '350px' }}>
                                                <TextField.Root style={{ width: '100%' }}>
                                                    <TextField.Input 
                                                        type="number"
                                                        placeholder="Bits para subnetting"
                                                        value={formData.subnettingBits}
                                                        onChange={(e) => handleInputChange('subnettingBits', e.target.value)}
                                                        min="1"
                                                        max="32"
                                                    />
                                                </TextField.Root>
                                            </Flex>
                                            <Text size="2" style={{ 
                                                marginLeft: '8px', 
                                                color: isDarkTheme ? '#94a3b8' : '#666',
                                                width: '200px' 
                                            }}>Cantidad de bits para subnetting</Text>
                                        </Flex>
                                    )}
                                    
                                    <Flex gap="1" align="center" style={{ maxWidth: '600px' }}>
                                        <Flex style={{ width: '350px' }}>
                                            <TextField.Root style={{ width: '100%' }}>
                                                <TextField.Input 
                                                    type="number"
                                                    placeholder="Número de subred"
                                                    value={formData.subnetNumber}
                                                    onChange={(e) => handleInputChange('subnetNumber', e.target.value)}
                                                />
                                            </TextField.Root>
                                        </Flex>
                                        <Text size="2" style={{ 
                                            marginLeft: '8px', 
                                            color: isDarkTheme ? '#94a3b8' : '#666',
                                            width: '200px' 
                                        }}>Número de subred a calcular</Text>
                                    </Flex>
                                    
                                    <Flex gap="2" style={{ width: '350px' }}>
                                        <Button size="2" style={{ flex: 1 }} onClick={handleCalculate}>
                                            Calcular Subred
                                        </Button>
                                        <Button 
                                            size="2" 
                                            variant="soft" 
                                            onClick={handleClean}
                                            style={{ 
                                                color: isDarkTheme ? '#94a3b8' : 'inherit'
                                            }}
                                        >
                                            Limpiar
                                        </Button>
                                    </Flex>
                                </Flex>
                            </Card>

                            {result && (
                                <Card style={{
                                    background: isDarkTheme ? '#2f3133' : '#f8f9fa',
                                    padding: '1.5rem',
                                    borderRadius: '8px'
                                }}>
                                    <Flex direction="column" gap="4">
                                        <Text size="2" weight="bold" style={{ 
                                            color: isDarkTheme ? '#94a3b8' : 'inherit' 
                                        }}>Resultados del cálculo</Text>
                                        
                                        {result.binaryVisualization && (
                                            <BinaryVisualization 
                                                octets={result.binaryVisualization.octets}
                                                colors={result.binaryVisualization.colors}
                                                isDarkTheme={isDarkTheme}
                                            />
                                        )}
                                        
                                        <Text style={{ color: isDarkTheme ? '#94a3b8' : 'inherit' }}>
                                            Número de subred {result.subnetNumber} en binario ({result.subnetNumber-1}): {result.binaryRepresentation}
                                        </Text>
                                        
                                        <Box style={{ 
                                            background: isDarkTheme ? '#1a1b1e' : 'white', 
                                            padding: '1rem', 
                                            borderRadius: '6px' 
                                        }}>
                                            <Flex direction="column" gap="2">
                                                <Text weight="bold" style={{ color: isDarkTheme ? '#94a3b8' : 'inherit' }}>
                                                    Red resultante: {result.resultNetwork}
                                                </Text>
                                                {result.networkType && (
                                                    <Text style={{ 
                                                        color: isDarkTheme ? '#94a3b8' : 'inherit',
                                                        fontStyle: 'italic'
                                                    }}>
                                                        {result.networkType}
                                                    </Text>
                                                )}
                                                <Text style={{ color: isDarkTheme ? '#94a3b8' : 'inherit' }}>
                                                    Primera dirección utilizable: {result.firstUsableAddress}
                                                </Text>
                                                <Text style={{ color: isDarkTheme ? '#94a3b8' : 'inherit' }}>
                                                    Última dirección utilizable: {result.lastUsableAddress}
                                                </Text>
                                                <Text style={{ color: isDarkTheme ? '#94a3b8' : 'inherit' }}>
                                                    Default Gateway: {result.defaultGateway}
                                                </Text>
                                                <Text style={{ color: isDarkTheme ? '#94a3b8' : 'inherit' }}>
                                                    Broadcast: {result.broadcast}
                                                </Text>
                                                <Text style={{ color: isDarkTheme ? '#94a3b8' : 'inherit' }}>
                                                    Hosts utilizables: {result.totalUsableHosts - (result.broadcast === 'N/A' ? 0 : 3)} 
                                                    {result.broadcast === 'N/A' 
                                                        ? ' (2 direcciones utilizables para enlaces punto a punto)'
                                                        : ` (${result.totalUsableHosts} direcciones totales - 1 dirección de subred - 1 dirección de broadcast - 1 dirección de gateway)`
                                                    }
                                                </Text>
                                            </Flex>
                                        </Box>
                                    </Flex>
                                </Card>
                            )}
                        </Flex>
                    </Card>
                </Container>
            </Box>
        </Theme>
    );
} 