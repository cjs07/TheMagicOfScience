package com.deepwelldevelopment.tmos.core.asm;

import gnu.trove.map.hash.TObjectByteHashMap;
import gnu.trove.set.hash.THashSet;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.Iterator;

import static org.objectweb.asm.Opcodes.*;

public class ASMCore {

    static TObjectByteHashMap<String> hashes = new TObjectByteHashMap<String>(30, 1, (byte) 0);
    static THashSet<String> parsables;
    static byte[] reseearchManager = null;
    static byte[] shapedRecipes = null;

    static void init() {
    }

    static {
        //TODO: ADD CLASSES TO MODIFY TO HASHES
        hashes.put(ASMNames.CL_CRAFTING_MANAGER, (byte) 1);
    }

    static final ArrayList<String> workingPath = new ArrayList<String>();
    private static final String[] emptyList = {};

    static byte[] parse(String name, String transformedName, byte[] bytes) {
        workingPath.add(transformedName);
        workingPath.remove(workingPath.size()-1);
        return bytes;
    }

    static byte[] transform(int index, String name, String transformedName, byte[] bytes) {
        ClassReader cr = new ClassReader(bytes);
        switch (index) {
            case 1:
                return alterCraftingManager(bytes);
            default:
                return bytes;
        }
    }

    private static byte[] alterCraftingManager(byte[] bytes) {
        ClassNode cn = ASMHelper.createClassNode(bytes);
        MethodNode m = ASMHelper.findMethod(cn, ASMNames.MD_CRAFTING_FIND_MATCHING);

        Iterator<AbstractInsnNode> iterator = m.instructions.iterator();
        AbstractInsnNode n;
        int index = 0;
        while (iterator.hasNext()) {
            n = iterator.next();
            if (n.getOpcode() == ARETURN && index == 0) { //make sure only the first instance of ARETURN is counted
                LabelNode l7 = new LabelNode();
                m.instructions.insertBefore(n, new VarInsnNode(ALOAD, 1));
                m.instructions.insertBefore(n, new MethodInsnNode(INVOKESTATIC, "com/deepwelldevelopment/tmos/core/research/ResearchManager", "doesPlayerHaveCraftingRequisite", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/inventory/InventoryCrafting;)Z", false));
                m.instructions.insertBefore(n, new JumpInsnNode(IFNE, l7));
                m.instructions.insertBefore(n, new FieldInsnNode(GETSTATIC, "net/minecraft/item/ItemStack", "EMPTY", "Lnet/minecraft/item/ItemStack;"));
                m.instructions.insertBefore(n, new InsnNode(ARETURN));
                m.instructions.insertBefore(n, l7);
                m.instructions.insertBefore(n, new VarInsnNode(ALOAD, 4));
                m.instructions.insertBefore(n, new VarInsnNode(ALOAD, 1));
                m.instructions.insertBefore(n, new MethodInsnNode(INVOKEINTERFACE, "net/minecraft/item/crafting/IRecipe", "getCraftingResult", "(Lnet/minecraft/inventory/InventoryCrafting;)Lnet/minecraft/item/ItemStack;", true));

                index++;
            }
        }

        bytes = ASMHelper.createBytes(cn, ClassWriter.COMPUTE_FRAMES);
        return bytes;
    }

    static void substitute(MethodNode toReplace, MethodNode with) {
        toReplace.instructions.clear();
        toReplace.localVariables = null;
        if (toReplace.name.equals(with.name) && toReplace.desc.equals(with.desc)) {
            toReplace.instructions.add(with.instructions);
        }
        Type rType = Type.getReturnType(toReplace.desc);
        switch (rType.getSort()) {
            case Type.METHOD:
            case Type.ARRAY:
            case Type.OBJECT:
                toReplace.instructions.add(new InsnNode(ACONST_NULL));
                break;
            case Type.FLOAT:
                toReplace.instructions.add(new InsnNode(FCONST_0));
                break;
            case Type.DOUBLE:
                toReplace.instructions.add(new InsnNode(DCONST_0));
                break;
            case Type.LONG:
                toReplace.instructions.add(new InsnNode(LCONST_0));
                break;
            default:
                toReplace.instructions.add(new InsnNode(ICONST_0));
                switch (rType.getSort()) {
                    case Type.SHORT:
                        toReplace.instructions.add(new InsnNode(I2S));
                        break;
                    case Type.CHAR:
                        toReplace.instructions.add(new InsnNode(I2C));
                        break;
                    case Type.BYTE:
                        toReplace.instructions.add(new InsnNode(I2B));
                        break;
                }
                break;
            case Type.VOID:
                break;
        }
        toReplace.instructions.add(new InsnNode(rType.getOpcode(IRETURN)));
    }

    static void scrapeData(ASMDataTable table) {

    }
}
