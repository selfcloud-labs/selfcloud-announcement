package pl.selfcloud.announcement.infrastructure.security.util;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SecurityConstants {

  HEADER("HEADER"), ISSUER("ISSUER"), KEY("KEY"), PREFIX("PREFIX");

  final String value;

}