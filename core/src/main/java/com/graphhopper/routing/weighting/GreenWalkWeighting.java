/*
 *  Licensed to GraphHopper GmbH under one or more contributor
 *  license agreements. See the NOTICE file distributed with this work for
 *  additional information regarding copyright ownership.
 *
 *  GraphHopper GmbH licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except in
 *  compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.graphhopper.routing.weighting;

import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.GreenWalkFlagEncoder;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.PMap;

public class GreenWalkWeighting extends PriorityWeighting {
    private final double minFactor;

    public GreenWalkWeighting(FlagEncoder flagEncoder, PMap pMap) {
        super(flagEncoder, pMap);

        minFactor = 0.2;
    }

    @Override
    public double getMinWeight(double distance) {
        return minFactor * distance;
    }

    @Override
    public double calcWeight(EdgeIteratorState edge, boolean reverse, int prevOrNextEdgeId) {
        double priority = flagEncoder.getDouble(edge.getFlags(), KEY);
        double greenness = flagEncoder.getDouble(edge.getFlags(), GreenWalkFlagEncoder.GREENNESS_KEY);

        double distance = edge.getDistance();
        double factor = 1 / (0.5 + priority);
        if (greenness > 0) {
            factor *= 1 / greenness;
        }

        factor = Math.max(factor, minFactor);

        return factor * distance;
    }

    @Override
    public String getName() {
        return "green_walk_weighting";
    }
}
