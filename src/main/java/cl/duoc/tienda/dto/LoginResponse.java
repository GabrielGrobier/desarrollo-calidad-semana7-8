package cl.duoc.tienda.dto;
public record LoginResponse(String tokenType, String accessToken, long expiresInSeconds) { }
