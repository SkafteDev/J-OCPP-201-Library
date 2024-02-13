package com.decouplink;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>Denotes that objects of the annotated type supports sharing among
 * multiple, possible independent, clients. The annotation is first and foremost
 * a means of documentation, but it may also be used to enforce sharing
 * policies.</p>
 *
 * <p>It is recommended that objects added using dynamic links are @Shareable.
 * </p>
 *
 * @author mrj
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Shareable {

}
