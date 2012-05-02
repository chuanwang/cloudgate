var tabPanel = new Ext.TabPanel({
			activeTab : 0,
            region : 'center',
			listeners : {
				tabchange : function(tp, newTab) {
                    newTab.doLayout();
                    /*
					var um = newTab.getUpdater();
					if (um)
						um.refresh();
                        */
				}
			},

			items : [dashboard/*, fileManager*/]
		});

function buildViewPort() {

    Ext.QuickTips.init();
    Ext.WindowMgr.zseed = 15000;
    
	var viewPort = new Ext.Viewport({
				layout : 'border',
				items : [tabPanel]
			});
}

Ext.onReady(buildViewPort);