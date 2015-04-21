/**
 * Copyright 2011-2015 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.util.matcher.Matcher;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class BundlePropertyBuilderRepository {

    private final Map<Matcher<ASTType>, PropertyBuilder> builders = new LinkedHashMap<Matcher<ASTType>, PropertyBuilder>();

    public void add(Matcher<ASTType> matcher, PropertyBuilder builder){
        builders.put(matcher, builder);
    }

    public boolean matches(ASTType type){
        for (Matcher<ASTType> matcher : builders.keySet()) {
            if(matcher.matches(type)){
                return true;
            }
        }
        return false;
    }

    public PropertyBuilder get(ASTType type) {
        for (Map.Entry<Matcher<ASTType>, PropertyBuilder> generatorEntry : builders.entrySet()) {
            if(generatorEntry.getKey().matches(type)){
                return generatorEntry.getValue();
            }
        }
        return null;
    }
}
