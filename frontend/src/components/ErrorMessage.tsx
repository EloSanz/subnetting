import React from 'react';
import { Text, Card, Flex } from '@radix-ui/themes';

interface ErrorMessageProps {
    message: string;
    onDismiss?: () => void;
}

export const ErrorMessage: React.FC<ErrorMessageProps> = ({ message, onDismiss }) => {
    return (
        <Card style={{ 
            backgroundColor: '#fee2e2', 
            border: '1px solid #ef4444',
            padding: '12px',
            marginBottom: '16px'
        }}>
            <Flex justify="between" align="center">
                <Text size="2" style={{ color: '#dc2626' }}>
                    {message}
                </Text>
                {onDismiss && (
                    <Text 
                        size="2" 
                        style={{ 
                            color: '#dc2626', 
                            cursor: 'pointer',
                            userSelect: 'none'
                        }}
                        onClick={onDismiss}
                    >
                        ✕
                    </Text>
                )}
            </Flex>
        </Card>
    );
}; 