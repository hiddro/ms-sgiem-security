package com.sgiem.ms.security.utils.commons;

import com.sgiem.ms.security.dto.RolDetails;
import com.sgiem.ms.security.dto.RolResponse;
import com.sgiem.ms.security.models.entity.UserCredential;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Commons {

    public static String generateCode(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 8;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString()
                .toUpperCase();

        return generatedString;
    }

    public static RolDetails.TituloEnum validateTitulo(String titulo){

        return titulo.equalsIgnoreCase("USER") ? RolDetails.TituloEnum.USER :
                titulo.equalsIgnoreCase("REHU") ? RolDetails.TituloEnum.REHU :
                        titulo.equalsIgnoreCase("ADMIN") ? RolDetails.TituloEnum.ADMIN : RolDetails.TituloEnum.USER;

    }

    public static RolResponse.TituloEnum validateTituloResponse(String titulo){

        return titulo.equalsIgnoreCase("USER") ? RolResponse.TituloEnum.USER :
                titulo.equalsIgnoreCase("REHU") ? RolResponse.TituloEnum.REHU :
                        titulo.equalsIgnoreCase("ADMIN") ? RolResponse.TituloEnum.ADMIN : RolResponse.TituloEnum.USER;

    }

    public static List<RolDetails> validateRolArray (UserCredential user) {
        return user.getRoles().isEmpty() ? new ArrayList<>() :
                user.getRoles()
                        .stream()
                        .map(r -> RolDetails.builder()
                                .idRol(r.getIdRol())
                                .titulo(validateTitulo(r.getTitulo()))
                                .idUser(r.getUser().getIdUser()).build())
                        .collect(Collectors.toList());
    }

    public static void validateArrayRoles(String titulo, UserCredential user){
        user.getRoles().forEach(r -> {
            if(r.getTitulo().equalsIgnoreCase(titulo)){
                throw new RuntimeException("El rol ya esta asignado al usuario!");
            }
        });
    }
}
