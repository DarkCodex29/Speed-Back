package com.hochschild.speed.back;

import com.hochschild.speed.back.model.domain.speed.Rol;
import com.hochschild.speed.back.repository.speed.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@PropertySource(value = {"classpath:application.properties", "classpath:mail.properties", "classpath:jdbc.properties"})
public class SpeedBackApplication extends SpringBootServletInitializer implements CommandLineRunner {

	@Autowired
	private RolRepository rolRepository;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SpeedBackApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(SpeedBackApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Debug completado - roles listados exitosamente
	}

	private void imprimirRolesDelSistema() {
		try {
			String separador = "================================================================================";
			System.out.println();
			System.out.println(separador);
			System.out.println("📋 LISTADO COMPLETO DE ROLES DEL SISTEMA - AUTO-EJECUTADO");
			System.out.println(separador);
			
			List<Rol> roles = rolRepository.getRolesActivos();
			
			if (roles != null && !roles.isEmpty()) {
				System.out.println("🔐 ROLES ENCONTRADOS: " + roles.size());
				System.out.println("--------------------------------------------------------------------------------");
				
				for (int i = 0; i < roles.size(); i++) {
					Rol rol = roles.get(i);
					System.out.println(String.format("(%d) ID: %d | CÓDIGO: '%s' | NOMBRE: '%s'", 
						i + 1, 
						rol.getId(), 
						rol.getCodigo() != null ? rol.getCodigo() : "SIN_CÓDIGO", 
						rol.getNombre()));
					
					if (rol.getDescripcion() != null && !rol.getDescripcion().trim().isEmpty()) {
						System.out.println("    📝 Descripción: " + rol.getDescripcion());
					}
					
					if (rol.getCodigoSCA() != null && !rol.getCodigoSCA().trim().isEmpty()) {
						System.out.println("    🏢 Código SCA: " + rol.getCodigoSCA());
					}
					
					System.out.println("    📅 Estado: " + rol.getEstado() + " | Fecha: " + rol.getFechaCreacion());
					System.out.println();
				}
			} else {
				System.out.println("❌ NO SE ENCONTRARON ROLES ACTIVOS");
			}
			
			System.out.println(separador);
			System.out.println("✅ LISTADO DE ROLES COMPLETADO");
			System.out.println(separador);
			System.out.println();
			
		} catch (Exception e) {
			System.out.println("❌ ERROR AL LISTAR ROLES: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods("POST", "PUT", "GET", "OPTIONS", "DELETE").exposedHeaders(HttpHeaders.CONTENT_DISPOSITION);
			}
		};
	}
}