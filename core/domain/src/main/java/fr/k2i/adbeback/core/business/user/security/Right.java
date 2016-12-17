package fr.k2i.adbeback.core.business.user.security;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * User: dimitri
 * Date: 12/01/15
 * Time: 11:40
 * Goal:
 */
@Getter
@RequiredArgsConstructor
public enum Right{
    READ("R"),WRITE("W"),DELETE("D"),ADD("N"),LIST("L");

    @NonNull private String persistentValue;


}
