package com.decouplink;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>Denotes that objects of the annotated type supports fault tolerance.
 * The annotation is used by the dynamic links library to transparently
 * implement fault tolerance for annotated interfaces.</p>
 *
 * @author mrj
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FaultTolerant {
}
