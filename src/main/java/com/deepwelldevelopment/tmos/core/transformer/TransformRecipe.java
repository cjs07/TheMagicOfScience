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
        if (name.equals(ASMNames.CL_BOOK_CLONING_RECIPE)) {

        } else if (name.equals(ASMNames.CL_FIREWORKS_RECIPE)) {

        } else if (name.equals(ASMNames.CL_REPAIR_ITEM_RECIPE)) {

        } else if (name.equals(ASMNames.CL_ARMOR_DYE_RECIPE)) {

        } else if (name.equals(ASMNames.CL_MAP_CLONING_RECIPE)) {

        } else if (name.equals(ASMNames.CL_TIPPED_ARROW_RECIPE)) {

        } else if (name.equals(ASMNames.CL_SHAPED_RECIPE)) {
            System.out.println("TRANSFORMING SHAPED RECIPES CLASS");
            return transformShaped(basicClass);
        } else if (name.equals(ASMNames.CL_SHAPELESS_RECIPE)) {
//            return transformShapeless(basicClass);
        } else if (name.equals("net/minecraft/item/crafting/CraftingManager")) {
            return transformFindMatching(basicClass);
        }
        return basicClass;
    }

    private byte[] transformFindMatching(byte[] bytes) {
        ClassNode classNode = ASMHelper.createClassNode(bytes);
        MethodNode method = ASMHelper.findMethod(classNode, ASMNames.MD_CRAFTING_FIND_MATCHING);

        InsnList needle = new InsnList();
        Iterator<AbstractInsnNode> iter  = method.instructions.iterator();
        AbstractInsnNode currentNode = iter.next();
        boolean found = false;
        while (iter.hasNext()) {
            if (currentNode.getOpcode() == Opcodes.ARETURN && !found) {
                InsnList injectList = new InsnList();
                injectList.add(ASMHelper.getFieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/item/ItemStack.EMPTY Lnet/minecraft/item/ItemStack"));
                injectList.add(ASMHelper.getMethodInsnNode(Opcodes.INVOKESTATIC, ASMNames.MD_TMOS_SHAPED_MATCHES_EX, false));
                injectList.add(new VarInsnNode(Opcodes.ISTORE, 5));
                injectList.add(ASMHelper.getFieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/item/ItemStack.EMPTY Lnet/minecraft/item/ItemStack"));
                injectList.add(new InsnNode(Opcodes.ARETURN));
                needle.add(injectList);
                currentNode = iter.next();
            } else {
                needle.add(currentNode);
                currentNode = iter.next();
            }
        }

        bytes = ASMHelper.createBytes(classNode, ClassWriter.COMPUTE_MAXS);
        return bytes;
    }

    private byte[] transformShaped(byte[] bytes) {
        ClassNode classNode = ASMHelper.createClassNode(bytes);

        //create the _TMOS_matches method
        MethodNode tmosMatches = ASMHelper.getMethodNode(Opcodes.ACC_PRIVATE, ASMNames.MD_TMOS_SHAPED_MATCHES);
        tmosMatches.visitCode();
        tmosMatches.visitInsn(Opcodes.ICONST_0);
        tmosMatches.visitInsn(Opcodes.IRETURN);
        tmosMatches.visitMaxs(1, 0);
        tmosMatches.visitEnd();

        classNode.methods.add(tmosMatches);

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
                injectList.add(ASMHelper.getFieldInsnNode(Opcodes.GETFIELD, ASMNames.FD_ITEMSTACK_RECIPE_OUTPUT_SHAPED));
                injectList.add(ASMHelper.getMethodInsnNode(Opcodes.INVOKESTATIC, ASMNames.MD_TMOS_SHAPED_MATCHES_EX, false));
                injectList.add(new InsnNode(Opcodes.IRETURN));
                needle.add(injectList);
                searchIndex++;
                currrentNode = iter.next();
            } else {
                needle.add(currrentNode);
                currrentNode = iter.next();
            }
        }

        bytes = ASMHelper.createBytes(classNode, ClassWriter.COMPUTE_MAXS);
        return bytes;
    }

    private byte[] transformShapeless(byte[] bytes) {
        ClassNode classNode = ASMHelper.createClassNode(bytes);
        MethodNode method = ASMHelper.findMethod(classNode, ASMNames.MD_SHAPELESS_MATCHES);

        InsnList needle = new InsnList();
        Iterator<AbstractInsnNode> iter  = method.instructions.iterator();
        AbstractInsnNode currrentNode = iter.next();
        int searchIndex = 0;
        while (iter.hasNext()) {
            if (currrentNode.getOpcode() == Opcodes.IRETURN) {
                if (searchIndex == 1) {
                    InsnList injectList = new InsnList();
                    injectList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    injectList.add(ASMHelper.getFieldInsnNode(Opcodes.GETFIELD, ASMNames.FD_ITEMSTACK_RECIPE_OUTPUT_SHAPELESS));
                    injectList.add(ASMHelper.getMethodInsnNode(Opcodes.INVOKESTATIC, ASMNames.MD_TMOS_SHAPED_MATCHES_EX, false));
                    injectList.add(new InsnNode(Opcodes.IRETURN));
                    needle.add(injectList);
                    searchIndex++;
                    currrentNode = iter.next();
                } else {
                    needle.add(currrentNode);
                    searchIndex++;
                }
            } else {
                needle.add(currrentNode);
                currrentNode = iter.next();
            }
        }

        bytes = ASMHelper.createBytes(classNode, ClassWriter.COMPUTE_MAXS);
        return bytes;
    }
}
