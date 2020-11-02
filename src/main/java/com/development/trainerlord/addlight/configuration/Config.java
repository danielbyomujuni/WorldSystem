package com.github.hexocraft.addlight.configuration;

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

import com.github.hexocraftapi.configuration.Configuration;
import com.github.hexocraftapi.configuration.annotation.ConfigFooter;
import com.github.hexocraftapi.configuration.annotation.ConfigHeader;
import com.github.hexocraftapi.configuration.annotation.ConfigPath;
import com.github.hexocraftapi.configuration.annotation.ConfigValue;
import org.bukkit.plugin.java.JavaPlugin;


@ConfigHeader(comment = {
"# ===--- AddLight ------------------------------------------------------------------------------------------------=== #",
"#                                                                                                                      ",
"#     Quickly add invisible light sources                                                                              ",
"#                                                                                                                      ",
"# ===------------------------------------------------------------------------------------------ © 2017 Hexosse ---=== #"
})
@ConfigFooter(comment = {
" ",
"# ===--- Enjoy -------------------------------------------------------------------------------- © 2017 Hexosse ---=== #"
})
public class Config extends Configuration
{
    /* Plugin */
    @ConfigPath(path = "plugin", comment = "*---------------- Plugin configuration --------------------*")
    @ConfigValue(path = "plugin.useMetrics", comment = "Enable metrics")        public boolean useMetrics = (boolean) true;
    @ConfigValue(path = "plugin.useUpdater", comment = "Enable updater")        public boolean useUpdater = (boolean) true;
    @ConfigValue(path = "plugin.downloadUpdate", comment = "Download update")   public boolean downloadUpdate = (boolean) true;

    /* ConnectedBlock */
    @ConfigPath(path = "connectedBlock", comment = "*-------------- ConnectedBlock configuration -----------------*")
    @ConfigValue(path = "connectedBlock.limit", comment = "Limit the number of connected blocks")
                                                                                public int cbLimit = 1024;

    /* Cost */
    @ConfigPath(path = "cost",  comment = "*------------ Charge player for adding lights ----------------*")
    @ConfigValue(path = "cost.glowstone-dust", comment = "if > 0 user will have to pay this amount of glowstone to add lights")
                                                                                public int costGlowstone = 6;
    @ConfigValue(path = "cost.money", comment = "if > 0 user will have to pay this amount of money to add lights (This require vault to be installed)")
                                                                                public int costMoney = 0;


    public Config(JavaPlugin plugin, String fileName, boolean load)
    {
        super(plugin, fileName);

        if(load) load();
    }
}
