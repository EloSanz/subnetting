# Subnet Calculator

Una aplicación web para calcular subredes IP, desarrollada con React (Frontend) y Spring Boot (Backend).

## Características

- Cálculo de subredes basado en dirección IP base y máscara
- Visualización binaria de bits de red, subred y host
- Cálculo automático de direcciones utilizables
- Interfaz moderna con soporte para tema claro/oscuro
- Validación completa de entradas
- Manejo de errores robusto

## Estructura del Proyecto

```
subnet-calculator/
├── frontend/           # Aplicación React
│   ├── src/
│   │   ├── components/  # Componentes React
│   │   ├── hooks/      # Custom hooks
│   │   ├── services/   # Servicios API
│   │   ├── types/      # TypeScript types
│   │   └── utils/      # Utilidades
│   └── package.json
└── backend/           # API Spring Boot
```

## Requisitos Previos

- Node.js >= 14.0.0
- Java JDK >= 17
- Maven >= 3.6

## Configuración del Entorno de Desarrollo

### Frontend

1. Navegar al directorio frontend:
   ```bash
   cd frontend
   ```

2. Instalar dependencias:
   ```bash
   npm install
   ```

3. Iniciar servidor de desarrollo:
   ```bash
   npm start
   ```

### Backend

1. Navegar al directorio backend:
   ```bash
   cd backend
   ```

2. Compilar el proyecto:
   ```bash
   mvn clean install
   ```

3. Ejecutar la aplicación:
   ```bash
   mvn spring-boot:run
   ```

## Configuración de Azure DevOps

1. Crear un nuevo proyecto en Azure DevOps
2. Inicializar el repositorio Git local y conectar con Azure:
   ```bash
   git init
   git add .
   git commit -m "Initial commit"
   git remote add origin <URL_DEL_REPOSITORIO_AZURE>
   git push -u origin main
   ```

3. Configurar el pipeline en `azure-pipelines.yml`

## Licencia

MIT 