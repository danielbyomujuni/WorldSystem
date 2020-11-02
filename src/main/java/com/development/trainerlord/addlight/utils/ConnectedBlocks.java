package com.github.hexocraft.addlight.utils;


/*
 * Copyright 2017 hexosse
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

//import com.github.hexocraftapi.util.AreaUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This file is part of AddLight
 *
 * @author <b>hexosse</b> (<a href="https://github.com/hexosse">hexosse on GitHub</a>).
 */
public class ConnectedBlocks
{
    private static BlockFace[] FACES;
	private static List<Location> unchecked = null;
	private static List<Location> confirmed = null;

    static { FACES = new BlockFace[]{BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST}; }

    public synchronized static List<Location> getConnectedBlocks(Location location, int limit)
    {
        return getConnectedBlocks(location, limit, null, null);
    }

    public synchronized static List<Location> getConnectedBlocks(Location location, int limit, Location corner1, Location corner2)
    {
        findConnectedBlocks(location.getBlock(), limit, corner1, corner2);
        return confirmed;
    }

    public synchronized static List<Location> getAdjacentTransparentBlocks(Location location)
    {
        List<Location> adjacents = new ArrayList<>(1);

        Block block = location.getBlock();
        for(BlockFace face : FACES)
        {
            Block relative = block.getRelative(face);
            if(relative.getType().isTransparent() || !relative.getType().isOccluding())
                adjacents.add(relative.getLocation());
        }

        return adjacents;
    }

    private static int findConnectedBlocks(Block block, int limit, Location corner1, Location corner2)
    {
        unchecked = Collections.synchronizedList(new ArrayList<Location>());
        confirmed = Collections.synchronizedList(new ArrayList<Location>());
        unchecked.add(block.getLocation());

        while(unchecked.size() > 0 && confirmed.size() <= (limit > 0 ? limit : unchecked.size()))
        {
            Location uncheckedLocation = unchecked.get(0);
            Block uncheckedBlock = unchecked.get(0).getBlock();

            if(!isValid(uncheckedBlock, block, corner1, corner2))
            {
                unchecked.remove(uncheckedLocation);
            }
            else
            {
                unchecked.remove(uncheckedLocation);
                confirmed.add(uncheckedLocation);

                for (BlockFace face : FACES)
                {
                    Block candidate = uncheckedBlock.getRelative(face);

                    if(isValid(candidate, block, corner1, corner2) && !isUnchecked(candidate) && !isConfirmed(candidate))
                        unchecked.add(candidate.getLocation());
                }

            }
        }
        return confirmed.size();
    }

    // the block toCheck must be of the same material as block
    // and must have one face exposed to transparent block
    protected static boolean isValid(Block toCheck, Block block, Location corner1, Location corner2)
    {
        if(!toCheck.getState().getType().equals(block.getState().getType()))
            return false;
        if(!toCheck.getState().getData().equals(block.getState().getData()))
            return false;

        for(BlockFace face : FACES)
        {
            Block relative = toCheck.getRelative(face);

            if( (relative.getType().isTransparent() || !relative.getType().isOccluding())
                && ( (corner1==null && corner2==null) || (AreaUtil.isInside(toCheck.getLocation(), corner1, corner2)) ) )
            {
                return true;
            }
        }
        return false;
    }

    // Test if the location is in the unchecked list
    protected static boolean isUnchecked(Location location)
    {
        for(Location uncheckedLocation : unchecked) {
            if(uncheckedLocation.equals(location))
                return true;
        }
        return false;
    }

    // Test if the block is in the unchecked list
    protected static boolean isUnchecked(Block block)
    {
        return isUnchecked(block.getLocation());
    }

    // Test if the block is in the confirmed list
    protected static boolean isConfirmed(Location location)
    {
        for(Location confirmedLocation : confirmed) {
            if(confirmedLocation.equals(location))
                return true;
        }
        return false;
    }

    // Test if the block is in the confirmed list
    protected static boolean isConfirmed(Block block)
    {
        return isConfirmed(block.getLocation());
    }
}
