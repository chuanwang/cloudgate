Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

// create some portlet tools using built in Ext tool ids
var tools = [{
			id : 'gear',
			handler : function() {
				Ext.Msg.alert('Message', 'The Settings tool was clicked.');
			}
		}, {
			id : 'close',
			handler : function(e, target, panel) {
				panel.ownerCt.remove(panel, true);
			}
		}];

Ext.chart.Chart.CHART_URL = './javascript/ext/resources/charts.swf';

var cpuStore = new Ext.data.JsonStore({
			url : './resources/cpustatus',
			fields : ['type', 'usage']
		});
cpuStore.load();

var cpuStatusPanel = new Ext.Panel({
			id : 'cpustatus',
			title : 'CPU statistics',
			columnWidth : 0.3,
			height : 200,
			frame : true,
			items : {
				store : cpuStore,
				xtype : 'piechart',
				dataField : 'usage',
				categoryField : 'type',
				extraStyle : {
					legend : {
						display : 'bottom',
						padding : 5,
						font : {
							family : 'Tahoma',
							size : 10
						}
					}
				}
			}
		});

var cpuSeriesStore = new Ext.data.JsonStore({
			url : './resources/cpuseries',
			fields : ['timestamp', 'idle', 'user', 'system']
		});
cpuSeriesStore.load();

var cpuSeriesPanel = new Ext.Panel({
			id : 'cpuseries',
			title : 'CPU Usage in last minute',
			columnWidth : 0.7,
			height : 200,
			frame : true,
			items : {
				store : cpuSeriesStore,
				xtype : 'linechart',
				xField : 'timestamp',

				series : [{
							yField : 'idle',
							displayName : 'idle',
							style : {
								size : 1,
								lineSize : 1
							}
						}, {
							yField : 'user',
							displayName : 'user',
							style : {
								size : 1,
								lineSize : 1
							}
						}, {
							yField : 'system',
							displayName : 'system',
							style : {
								size : 1,
								lineSize : 1
							}
						}],
				extraStyle : {
					legend : {
						display : 'bottom',
						padding : 5,
						font : {
							family : 'Tahoma',
							size : 10
						}
					}
				}
			}

		});

var memoryStore = new Ext.data.JsonStore({
			url : './resources/memorystatus',
			fields : ['type', 'usage']
		});
memoryStore.load();

var memoryStatusPanel = new Ext.Panel({
			id : 'memorystatus',
			title : 'Memory statistics',
			columnWidth : 0.3,
			height : 200,
			frame : true,
			items : {
				store : memoryStore,
				xtype : 'piechart',
				dataField : 'usage',
				categoryField : 'type',
				extraStyle : {
					legend : {
						display : 'bottom',
						padding : 5,
						font : {
							family : 'Tahoma',
							size : 10
						}
					}
				}
			}
		});

var memorySeriesStore = new Ext.data.JsonStore({
			url : './resources/memoryseries',
			fields : ['timestamp', 'free', 'used']
		});
memorySeriesStore.load();

var memorySeriesPanel = new Ext.Panel({
			id : 'memoryseries',
			title : 'Memory Usage in last minute',
			columnWidth : 0.7,
			height : 200,
			frame : true,
			region : 'center',
			items : {
				store : memorySeriesStore,
				xtype : 'linechart',
				xField : 'timestamp',

				series : [{
							yField : 'free',
							displayName : 'free',
							style : {
								size : 1,
								lineSize : 1
							}
						}, {
							yField : 'used',
							displayName : 'used',
							style : {
								size : 1,
								lineSize : 1
							}
						}],
				extraStyle : {
					legend : {
						display : 'bottom',
						padding : 5,
						font : {
							family : 'Tahoma',
							size : 10
						}
					}
				}
			}

		});

var cpuWidgetContainer = new Ext.Panel({
			id : 'cpuwidgetcontainer',
			layout : 'column',
			height : 200,
			region : 'north',
			items : [cpuStatusPanel, cpuSeriesPanel]
		});

var memoryWidgetContainer = new Ext.Panel({
			id : 'memorywidgetcontainer',
			layout : 'column',
			height : 200,
			region : 'center',
			items : [memoryStatusPanel, memorySeriesPanel]
		});

var sectorStatusStore = new Ext.data.JsonStore({
			url : './resources/sectorstatus',
			fields : ['masters', 'availableDisk', 'runningSince',
					'fileSize', 'fileNumber', 'slaveNodeNumber',
					'clusterNumber']
		});
sectorStatusStore.load();

var sectorStatusTpl = new Ext.XTemplate('<tpl for=".">',
		'<div class="sector-status">', '<pre>',
		'<b>Running Since:</b>               {runningSince}<br>',
		'<b>Available Disk Size:</b>         {availableDisk}<br>',
		'<b>Total File Size:</b>             {fileSize}<br>',
		'<b>Total Number of Files:</b>       {fileNumber}<br>',
		'<b>Total Number of Slave Nodes:</b> {slaveNodeNumber}<br>',
		'<b>Total Number of Clusters:</b>    {clusterNumber}<br>', '</pre>',
		'</div>', 
		'<div class="sector-status">', '<pre>',
		'<b>Master ID    IP:Port</b>',
		'<br/>',
		'<tpl for="masters">',
		'{id}         {address}:{port}',
		'<br/>',
		'</tpl>', 
		'</pre>', '</div>',
		'</tpl>')

var sectorStatusView = new Ext.DataView({
			store : sectorStatusStore,
			tpl : sectorStatusTpl,
			autoHeight : true
		});

var clusterStatusStore = new Ext.data.JsonStore({
			url : './resources/clusterstatus',
			fields : [{
						name : 'id',
						type : 'int'
					}, {
						name : 'nodeNumber',
						type : 'int'
					}, {
						name : 'availableDisk'
					}, {
						name : 'fileSize'
					}, {
						name : 'netIn'
					}, {
						name : 'netOut'
					}, {
						name : 'slaveNodes',
						type : 'auto'
					} 
					]
		});
clusterStatusStore.load();

var clusterStatusTpl = new Ext.XTemplate(
		'<tpl for=".">',
		'<div class="cluster-status">',
		'<pre class="cluster-info">',
		'<b>Cluster ID:</b> {id} <b>Total Nodes:</b> {nodeNumber}<br>',
		'<b>Available Disk Size:</b> {availableDisk} <b>Total File Size:</b> {fileSize}<br>',
		'<b>Net In:</b> {netIn} <b>Net Out:</b> {netOut}<br>', '</pre>',
		'<tpl for="slaveNodes">',
		'<span class="slave-block {status}" id="{id}"></span>', '</tpl>',
		'<div class="cluster-status-clear"></div>', '</div>', '</tpl>');

var clickNodeHandler = function(sourceView, index, node, e) {
	var id = node.id;
	var slaveNodeStore = new Ext.data.JsonStore({
				url : './resources/slave/' + id,
				fields : ['id', 'port', 'clusterId', 'status', 'address',
						'availableDisk', 'fileSize', 'memory', 'cpu', 'netIn',
						'netOut', 'lastUpdate', 'dataDir']  
			});
	slaveNodeStore.load();

	var slaveNodeTpl = new Ext.XTemplate('<tpl for=".">', '<div>', '<pre>',
			'<b>Slave ID:</b>                    {id}<br>',
			'<b>Cluster ID:</b>                  {clusterId}<br>',
			'<b>Status:</b>                      {status}<br>',
			'<b>IP Address:</b>                  {address}:{port}<br>',
			'<b>Available Disk Size:</b>         {availableDisk}<br>',
			'<b>File Size:</b>                   {fileSize}<br>',
			'<b>Memory:</b>                      {memory}<br>',
			'<b>CPU:</b>                         {cpu}<br>',
			'<b>NetIn:</b>                       {netIn}<br>',
			'<b>NetOut:</b>                      {netOut}<br>',
			'<b>Data Directory:</b>              {dataDir}<br>', 
			'</pre>',
			'</div>', '</tpl>');

	var slaveNodeWindow = new Ext.Window({
				title : 'Slave Node ' + id + ' Status',
				width : 460,
				height : 260,
				items : new Ext.DataView({
							store : slaveNodeStore,
							tpl : slaveNodeTpl,
							authHeight : true
						}),

				buttons : [{
							text : 'Close',
							handler : function() {
								slaveNodeWindow.close();
							}
						}]
			});

	slaveNodeWindow.show(node);
}

var clusterStatusView = new Ext.DataView({
			store : clusterStatusStore,
			tpl : clusterStatusTpl,
			autoHeight : true,
			listeners : {
				click : clickNodeHandler
				//(dataView, index, node, e)

				/*
				function(dataView, index, node, e) {
				           var id = node.id;
				          Ext.Msg.alert('Slave node ' + id + " is clicked.");
				        }
				}*/
			}
		});

var dashboard = new Ext.ux.Portal({
			id : 'portal',
			//title : 'CouldGate Dashboard',
			title : 'Test',
			//region : 'center',
			items : [/*{
						title : 'column1',
						columnWidth : .50,
						style : 'padding:10px 0 10px 10px',
						items : [{
									title : 'System Status',
									tools : tools,
									height : 500,
									layout : 'border',

									tbar : [{
												text : 'Refresh',
												handler : function() {
													cpuStore.load();
													cpuSeriesStore.load();
													memoryStore.load();
													memorySeriesStore.load();
												}
											}],
									items : [cpuWidgetContainer,
											memoryWidgetContainer]
								},
								{
									title : 'Nodes Topology',
									tools : tools,
									height : 300,
									layout : 'fit',
									items : [ topoGrid ]
								}
								]
					},*/ {
						title : 'column2',
						columnWidth : .50,
						style : 'padding:10px 0 10px 10px',
						items : [/*{
									title : 'Sector Status Information',
									tools : tools,
									autoHeight : true,
									items : [sectorStatusView,
											clusterStatusView]
								},*/
								{
									/*title : 'Sector Users',*/
									tools : tools,
									height : 300,
									layout : 'fit',
									items : [ {xtype : 'sectorusersgridpanel'} ]
								}
								]
					}]
		});