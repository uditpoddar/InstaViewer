package com.uditpoddar.android.instaviewer;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by upoddar on 11/10/15.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class InstaPicComment {
    private final String username;
    private final String comment;
}
