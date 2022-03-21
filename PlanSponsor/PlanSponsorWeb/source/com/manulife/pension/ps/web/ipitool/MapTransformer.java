package com.manulife.pension.ps.web.ipitool;

import java.util.Map;


/**
 * @param <S> specification class that helps FieldAttribute identify a key in the source Map and directs Builder on what to do with the source Map value
 * @param <K> key class of source Map
 * @param <V> value class of source Map
 */
class MapTransformer<S, K, V> {
    
    static interface FieldAttribute<S, K> {
        K getAttribute(S spec);
    }
    
    static interface Builder<V, S> {
        void build(V input, S spec);
    }
    
    private final Iterable<S> specSet;
    private final FieldAttribute<S, K> keyRecognizer;
    
    MapTransformer(
            Iterable<S> specSet,
            FieldAttribute<S, K> keyRecognizer) {
        this.specSet = specSet;
        this.keyRecognizer = keyRecognizer;
    }
    
    void transform(
            Map<K, V> input,
            Builder<V, S> builder) {
        
        for (S spec : specSet) {
            
            V value = input.get(keyRecognizer.getAttribute(spec));
            if (value != null) {
                builder.build(value, spec);
            }
            
        }
    }
    
}

