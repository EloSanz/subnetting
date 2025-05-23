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
- Git

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

## Control de Versiones

Este proyecto usa Git para el control de versiones. Para comenzar a trabajar:

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/EloSanz/subnetting.git
   cd subnet-calculator
   ```

2. Crear una nueva rama para tus cambios:
   ```bash
   git checkout -b feature/nueva-caracteristica
   ```

3. Realizar cambios y confirmarlos:
   ```bash
   git add .
   git commit -m "descripción de los cambios"
   ```

4. Subir los cambios:
   ```bash
   git push origin feature/nueva-caracteristica
   ```

### Convenciones de Commits

Para mantener un historial de commits limpio y comprensible, seguimos estas convenciones:

- `feat: ` para nuevas características
- `fix: ` para correcciones de bugs
- `docs: ` para cambios en la documentación
- `style: ` para cambios que no afectan el código (formato, espacios, etc)
- `refactor: ` para refactorización de código
- `test: ` para añadir o modificar tests
- `chore: ` para tareas de mantenimiento

Ejemplo:
```bash
git commit -m "feat: añadir validación de máscara de red"
git commit -m "fix: corregir cálculo de hosts disponibles"
```

## Licencia

MIT 

## Deployment

### Backend (Render.com)

1. Create a new Web Service in Render
2. Connect your GitHub repository
3. Use the following settings:
   - Name: subnet-calculator-api
   - Environment: Java
   - Build Command: `./mvnw clean install -DskipTests`
   - Start Command: `java -jar target/*.jar`
   - Instance Type: Free

### Frontend (Render.com)

1. Create a new Static Site in Render
2. Connect your GitHub repository
3. Use the following settings:
   - Name: subnet-calculator
   - Build Command: `npm install && npm run build`
   - Publish Directory: `dist`
4. Add the following environment variable:
   - Key: `VITE_API_URL`
   - Value: Your backend URL (e.g., https://subnet-calculator-api.onrender.com)

The application will be available at your Render-provided URL. 