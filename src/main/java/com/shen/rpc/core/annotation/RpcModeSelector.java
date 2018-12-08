package com.shen.rpc.core.annotation;

import com.shen.rpc.core.RpcClientAutoConfiguration;
import com.shen.rpc.core.RpcServerAutoConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author shenjianeng
 * @date 2018/12/4
 */
public class RpcModeSelector implements ImportSelector {

    private static final String MODE_ATTRIBUTE_NAME = "mode";

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        AnnotationAttributes attributes = (AnnotationAttributes) annotationMetadata.getAnnotationAttributes(EnableRpc.class.getName());
        if (attributes == null) {
            return null;
        }

        final RpcMode rpcMode = attributes.getEnum(MODE_ATTRIBUTE_NAME);

        switch (rpcMode) {
            case SERVER:
                return new String[]{RpcServerAutoConfiguration.class.getName()};

            case CLIENT:
                return new String[]{RpcClientAutoConfiguration.class.getName()};

            case SERVER_AND_CLIENT:

                return new String[]{RpcServerAutoConfiguration.class.getName(), RpcClientAutoConfiguration.class.getName()};

            default:
                return new String[0];
        }
    }
}
