package com.deepwelldevelopment.tmos.core.init;

import com.deepwelldevelopment.tmos.core.mod.TMOSCoreDummyContainer;
import com.deepwelldevelopment.tmos.core.transformer.ASMHelper;
import com.deepwelldevelopment.tmos.core.transformer.TransformRecipe;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Map;


@IFMLLoadingPlugin.SortingIndex( 1001 )
@IFMLLoadingPlugin.MCVersion( TMOSLoadingPlugin.MC_VERSION )
@IFMLLoadingPlugin.DependsOn( "forge" )
@IFMLLoadingPlugin.TransformerExclusions( { "com.deepwelldevelopment.tmos.core.transformer", "com.deepwelldevelopment.tmos.core.init" } )
public class TMOSLoadingPlugin implements IFMLLoadingPlugin {

    public static final String MC_VERSION = "1.11.2";
    public static final String MOD_ID = "tmos";
    public static final Logger MOD_LOG = LogManager.getLogger(MOD_ID);
    public static final String MOD_VERSION = "0.0.1";

    public static File source;

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{TransformRecipe.class.getName()};
    }

    @Override
    public String getModContainerClass() {
        return TMOSCoreDummyContainer.class.getName();
    }


    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        ASMHelper.isMCP = !(Boolean) data.get("runtimeDeobfuscationEnabled");

        source = (File) data.get("coremodLocation");
        if( source == null ) {          // this is usually in a dev env
            try {
                source = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());

                if( !(new File(source, "assets")).exists() ) {          // fix for IntelliJ
                    source = new File(source.getParentFile().getParentFile(), "resources/main");
                }
            } catch( URISyntaxException e ) {
                throw new RuntimeException("Failed to acquire source location for TMOS Core!", e);
            }
        }
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
