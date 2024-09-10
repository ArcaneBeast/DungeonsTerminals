package us.dxtrus.commons.command;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    @NotNull String name();
    @NotNull String permission();
    @NotNull String[] aliases() default {};
    boolean inGameOnly() default true;
    boolean async() default false;
}