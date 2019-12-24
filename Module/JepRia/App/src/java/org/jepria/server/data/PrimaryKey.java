package org.jepria.server.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
/**
 * Аннотацией отмечается поле в классе Dto, являющееся первичным ключом сущности, которую этот Dto класс описывает.
 * В случае составного первичного ключа аннотацией отмечаются все поля, входящие в первичный ключ.
 * Если одно и то же поле (первичный ключ) присутствует в нескольких Dto-классах сущности (например, EntityDto и EntitySearchDto), достаточно пометить аннотацией первичный ключ в одном из Dto-классов
 */
public @interface PrimaryKey {
}
