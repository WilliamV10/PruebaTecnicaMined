package com.tramites.tramites.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI/Swagger para la documentación de la API.
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema Gubernamental de Gestión de Trámites Ciudadanos")
                        .version("1.0.0")
                        .description("""
                                API REST para el Sistema Gubernamental de Gestión de Trámites Ciudadanos.
                                
                                Este sistema permite:
                                - Registro y gestión de ciudadanos
                                - Gestión de catálogos de tipos de documento y tipos de trámite
                                - Registro y seguimiento de trámites ciudadanos
                                
                                **Características técnicas:**
                                - API REST versionada (/api/v1)
                                - Soft delete para todos los recursos
                                - Validaciones de datos
                                - Respuestas estandarizadas
                                """))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Servidor de Desarrollo")
                ));
    }
}
