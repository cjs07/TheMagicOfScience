package com.deepwelldevelopment.tmos.core.transformer;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Iterator;

import static com.deepwelldevelopment.tmos.core.init.TMOSLoadingPlugin.MOD_LOG;

public class TransformRecipe implements IClassTransformer {

    boolean returnVal = true;

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (name.equals("net.minecraft.item.crafting.ShapedRecipes")) {
            System.out.println("********* INSIDE RECIPE TRANSFORMER ABOUT TO PATCH: " + name);
            return transformShaped(basicClass);
        }
        return basicClass;
    }

    private byte[] transformShaped(byte[] bytes) {
        ClassNode classNode = ASMHelper.createClassNode(bytes);
        MethodNode method = ASMHelper.findMethod(classNode, ASMNames.MD_SHAPED_MATCHES);

        MOD_LOG.info("TRANSFORMING  MATCHES METHOD");
        InsnList needle = new InsnList();
        Iterator<AbstractInsnNode> iter  = method.instructions.iterator();
        AbstractInsnNode currrentNode = iter.next();
        int searchIndex = 0;
        while (iter.hasNext()) {
            if (currrentNode.getOpcode() == Opcodes.IRETURN && searchIndex < 2) {
                MOD_LOG.info("FOUND A RETURN CALL IN THE METHOD");
                InsnList injectList = new InsnList();
                injectList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                injectList.add(ASMHelper.getFieldInsnNode(Opcodes.GETFIELD, ASMNames.FD_ITEMSTACK_RECIPE_OUTPUT));
                injectList.add(new VarInsnNode(Opcodes.ASTORE, 5));
                injectList.add(new VarInsnNode(Opcodes.ALOAD, 5));
                injectList.add(ASMHelper.getMethodInsnNode(Opcodes.INVOKESTATIC, ASMNames.MD_TMOS_SHAPED_MATCHES, false));
                injectList.add(new VarInsnNode(Opcodes.ASTORE, 6));
                injectList.add(new InsnNode(Opcodes.IRETURN));

                method.instructions.insert(currrentNode, injectList);
                searchIndex++;
                currrentNode = iter.next();
            } else {
                needle.add(currrentNode);
                currrentNode = iter.next();
            }
        }

        iter  = method.instructions.iterator();
        currrentNode = iter.next();
        while (iter.hasNext()) {
            if (currrentNode.getOpcode() == Opcodes.INVOKESTATIC) {
                MOD_LOG.info("FOUND A STATIC CALL");
            }
            currrentNode = iter.next();
        }

        bytes = ASMHelper.createBytes(classNode, ClassWriter.COMPUTE_MAXS);
        return bytes;
    }
}
