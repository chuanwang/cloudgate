var NodeTopology = Ext.data.Record.create([
	{
		name : 'nodes',
		type : 'string'
	},
	{
		name : 'topo',
		type : 'string'
	}
]);


var topoReader = new Ext.data.JsonReader({
	idProperty : 'nodes',
	root : 'rows',
	fields : [
		{name : 'nodes' },
		{name : 'topo' }
	]
});

var topoWriter = new Ext.data.JsonWriter({
	encode : true,
	writeAllFields : true
});

var proxy = new Ext.data.HttpProxy({
	url : '/resources/topology'
});

var topoStore = new Ext.data.Store({
	proxy : proxy,
	reader : topoReader,
	writer : topoWriter,
	autoSave : false,
	batch : true
});

var editor = new Ext.ux.grid.RowEditor({
	saveText : 'Update'
});

var topoGrid = new Ext.grid.GridPanel({
	store : topoStore,
	autoExpandColumn : 'nodes',
	plugins : [editor],
	// view : bufferedView
	
	tbar : [
	{
		text : 'Add Node(s)',
		handler : function() {
			var newNode = new NodeTopology({
				nodes : '0.0.0.0',
				topo : '/xxx/xxx'
			});
			editor.stopEditing();
			topoStore.insert(0, newNode);
			topoGrid.getView().refresh();
			topoGrid.getSelectionModel().selectRow(0);
			editor.startEditing();
		}
	},
	{
		ref : '../removeBtn',
		text : 'Remove Node(s)',
		disabled : true,
		handler : function() {
			editor.stopEditing();
			var s = topoGrid.getSelectionModel().getSelections();
			for(var i = 0, r ; r = s[i] ; i++) {
				topoStore.remove(r);
			}
		}
	}
	],

	columns : [
		{
            id: 'nodes',
            header: 'Node(s)',
            dataIndex: 'nodes',
            width: 220,
            sortable: true,
            editor: {
                xtype: 'textfield',
                allowBlank: false
            }
        },
        {
            id: 'topo',
            header: 'Topology',
            dataIndex: 'topo',
            width: 320,
            sortable: true,
            editor: {
                xtype: 'textfield',
                allowBlank: false
            }
        }
	]

});

topoGrid.getSelectionModel().on('selectionchange', function(sm) {
	topoGrid.removeBtn.setDisabled(sm.getCount() < 1);
});