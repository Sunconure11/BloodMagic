package WayofTime.bloodmagic.inversion;

import WayofTime.bloodmagic.api.soul.EnumDemonWillType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class InversionPillarHandler {
    public static final double farthestDistanceSquared = 16 * 16;
    public static Map<Integer, Map<EnumDemonWillType, List<BlockPos>>> pillarMap = new HashMap<Integer, Map<EnumDemonWillType, List<BlockPos>>>();
    public static Map<Integer, Map<EnumDemonWillType, Map<BlockPos, List<BlockPos>>>> nearPillarMap = new HashMap<Integer, Map<EnumDemonWillType, Map<BlockPos, List<BlockPos>>>>();

    public static boolean addPillarToMap(World world, EnumDemonWillType type, BlockPos pos) {
        int dim = world.provider.getDimension();
        if (pillarMap.containsKey(dim)) {
            Map<EnumDemonWillType, List<BlockPos>> willMap = pillarMap.get(dim);
            if (willMap.containsKey(type)) {
                if (!willMap.get(type).contains(pos)) {
                    willMap.get(type).add(pos);
                    onPillarAdded(world, type, pos);
                    return true;
                } else {
                    return false;
                }
            } else {
                List<BlockPos> posList = new ArrayList<BlockPos>();
                posList.add(pos);
                willMap.put(type, posList);
                onPillarAdded(world, type, pos);
                return true;
            }
        } else {
            Map<EnumDemonWillType, List<BlockPos>> willMap = new HashMap<EnumDemonWillType, List<BlockPos>>();
            List<BlockPos> posList = new ArrayList<BlockPos>();
            posList.add(pos);

            willMap.put(type, posList);
            pillarMap.put(dim, willMap);
            onPillarAdded(world, type, pos);
            return true;
        }
    }

    public static boolean removePillarFromMap(World world, EnumDemonWillType type, BlockPos pos) {
        int dim = world.provider.getDimension();
        if (pillarMap.containsKey(dim)) {
            Map<EnumDemonWillType, List<BlockPos>> willMap = pillarMap.get(dim);
            if (willMap.containsKey(type)) {
                if (willMap.get(type).contains(pos)) {
                    onPillarRemoved(world, type, pos);
                    return willMap.get(type).remove(pos);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    //Assume that it has been added already.
    private static void onPillarAdded(World world, EnumDemonWillType type, BlockPos pos) {
        System.out.println("Adding...");
        List<BlockPos> closePosList = new ArrayList<BlockPos>();

        int dim = world.provider.getDimension();
        if (pillarMap.containsKey(dim)) {
            Map<EnumDemonWillType, List<BlockPos>> willMap = pillarMap.get(dim);
            if (willMap.containsKey(type)) {
                List<BlockPos> otherPosList = willMap.get(type);

                for (BlockPos closePos : otherPosList) {
                    if (!closePos.equals(pos) && closePos.distanceSq(pos) <= farthestDistanceSquared) {
                        closePosList.add(closePos);
                    }
                }

            }
        }
        if (nearPillarMap.containsKey(dim)) {
            Map<EnumDemonWillType, Map<BlockPos, List<BlockPos>>> willMap = nearPillarMap.get(dim);
            if (willMap.containsKey(type)) {
                Map<BlockPos, List<BlockPos>> posMap = willMap.get(type);

                for (BlockPos closePos : closePosList) {
                    List<BlockPos> posList = posMap.get(closePos);
                    if (posList != null && !posList.contains(pos)) {
                        posList.add(pos);
                    } else {
                        posList = new ArrayList<BlockPos>();
                        posList.add(pos);
                        posMap.put(closePos, posList);
                    }
                }

                posMap.put(pos, closePosList);
            } else {
                Map<BlockPos, List<BlockPos>> posMap = new HashMap<BlockPos, List<BlockPos>>();

                posMap.put(pos, closePosList);
                willMap.put(type, posMap);
            }
        } else {
            Map<EnumDemonWillType, Map<BlockPos, List<BlockPos>>> willMap = new HashMap<EnumDemonWillType, Map<BlockPos, List<BlockPos>>>();
            Map<BlockPos, List<BlockPos>> posMap = new HashMap<BlockPos, List<BlockPos>>();

            posMap.put(pos, closePosList);
            willMap.put(type, posMap);
            nearPillarMap.put(dim, willMap);
        }
    }

    private static void onPillarRemoved(World world, EnumDemonWillType type, BlockPos pos) {
        System.out.println("Removing...");
        int dim = world.provider.getDimension();
        if (nearPillarMap.containsKey(dim)) {
            Map<EnumDemonWillType, Map<BlockPos, List<BlockPos>>> willMap = nearPillarMap.get(dim);
            if (willMap.containsKey(type)) {
                Map<BlockPos, List<BlockPos>> posMap = willMap.get(type);
                List<BlockPos> posList = posMap.get(pos);
                if (posList != null) {
                    Iterator<BlockPos> itr = posList.iterator();
                    while (itr.hasNext()) {
                        BlockPos checkPos = itr.next();
                        List<BlockPos> checkPosList = posMap.get(checkPos);
                        if (checkPosList != null) {
                            checkPosList.remove(pos);
                        }
                    }

                    posMap.remove(pos);
                }
            }
        }
    }

    //TODO: Change to use the nearPillarMap.
    public static List<BlockPos> getNearbyPillars(World world, EnumDemonWillType type, BlockPos pos) {
        int dim = world.provider.getDimension();
        if (nearPillarMap.containsKey(dim)) {
            Map<EnumDemonWillType, Map<BlockPos, List<BlockPos>>> willMap = nearPillarMap.get(dim);
            if (willMap.containsKey(type)) {
                Map<BlockPos, List<BlockPos>> posMap = willMap.get(type);
                List<BlockPos> posList = posMap.get(pos);
                if (posList != null) {
                    return posList;
                }
            }
        }

        return new ArrayList<BlockPos>();
    }

    public static List<BlockPos> getAllConnectedPillars(World world, EnumDemonWillType type, BlockPos pos) {
        List<BlockPos> checkedPosList = new ArrayList<BlockPos>();
        List<BlockPos> uncheckedPosList = new ArrayList<BlockPos>(); //Positions where we did not check their connections.

        uncheckedPosList.add(pos);

        int dim = world.provider.getDimension();
        if (nearPillarMap.containsKey(dim)) {
            Map<EnumDemonWillType, Map<BlockPos, List<BlockPos>>> willMap = nearPillarMap.get(dim);
            if (willMap.containsKey(type)) {
                Map<BlockPos, List<BlockPos>> posMap = willMap.get(type);
                // This is where the magic happens. 

                while (!uncheckedPosList.isEmpty()) {
                    //Positions that are new this iteration and need to be dumped into uncheckedPosList next iteration.
                    List<BlockPos> newPosList = new ArrayList<BlockPos>();

                    for (BlockPos checkPos : uncheckedPosList) {
                        List<BlockPos> posList = posMap.get(checkPos);
                        if (posList != null) {
                            for (BlockPos testPos : posList) {
                                //Check if the position has already been checked, is scheduled to be checked, or is already found it needs to be checked.
                                if (!checkedPosList.contains(testPos) && !uncheckedPosList.contains(testPos) && !newPosList.contains(testPos)) {
                                    newPosList.add(testPos);
                                }
                            }
                        }
                    }

                    checkedPosList.addAll(uncheckedPosList);
                    uncheckedPosList = newPosList;
                }
            }
        }

        return checkedPosList;
    }
}
