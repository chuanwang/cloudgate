Ext.ns("SectorUser");

SectorUser.SectorUsersGridPanel = Ext.extend(Ext.grid.GridPanel, {

	url : './resources/users',
	stripeRows : true,
	msgs : {
		deleteUserConfirm : 'Are you sure you want delete selected uesr(s)?',
		deletingUsers : 'Deleting user(s)'
	},
	
	initComponent : function() {
		
		this.sm = new Ext.grid.CheckboxSelectionModel();
		this.colModel = this.buildColModel();
		this.store = this.buildStore();
        this.tbar = this.buildTbar();
		
		SectorUser.SectorUsersGridPanel.superclass.initComponent.call(this)
	},
	
	buildColModel : function() {
		return new Ext.grid.ColumnModel({
			defaults : {
				width : 120,
				sortable : true
			},
		
			columns : [
				this.sm,
				{
					id : 'name',
					header : 'User Name',
					dataIndex : 'name'
				},
				{
					header : 'Quota',
					dataIndex : 'quota'
				},
				{
					header : 'Access Control List',
					dataIndex : 'acl'
				},
				{
					header : 'Execute Permission',
					dataIndex : 'exec_permission'
				},
				{
					header : 'Read Permission',
					dataIndex : 'read_permission'
				},
				{
					header : 'Write Permission',
					dataIndex : 'write_permission'
				}
			]
		
		});
		
	},
	
	buildStore : function() {
		return {
			xtype : 'jsonstore',
			url : this.url,
			autoLoad : true,
			root : 'rows',
			fields : [
			    {name : 'user_id'},
				{name : 'name'},
				{name : 'quota', type : 'int' },
				{name : 'acl'},
				{name : 'exec_permission', type : 'boolean'},
				{name : 'read_permission'},
				{name : 'write_permission'}
			],
			sortInfo : {
				field : 'name',
				direction : 'ASC'
			}
		};
	},

    buildTbar : function() {
        return [ 
            '->',
            {
				text : 'Add User',
				iconCls : 'icon-user-add',
				scope : this,
				handler : this.onAddUser
            },
            '-',
            {
				text : 'Edit User',
				iconCls : 'icon-user-edit',
				scope : this,
				handler : this.onEditUser
            },
             '-',
            {
				text : 'Delete User',
				iconCls : 'icon-user-delete',
				scope : this,
				handler : this.onDeleteUser
            }
        ]
    },
    
    onAddUser : function() {
    	var userWindow = new SectorUser.SectorUserWindow({
    		listeners : {
    			sectorusersaved : {
    				single : true,
    				scope : this,
    				fn : this.onSaveUserSuccess
    			}
    		}
    	}).show();
    	
    },
    
    onEditUser : function() {
    	var records = this.getSelections();
    	if(records.length > 0) {		
    		var r = records[0];
    		
    		var userWindow = new SectorUser.SectorUserWindow({
    			record : r,
    			listeners : {
    				sectorusersaved : {
    					single : true,
    					scope : this,
    					fn : this.onSaveUserSuccess
    				}
    			}
    			
    		}).show();
    	}
    },
    
    onSaveUserSuccess : function(action) {
    	var store = this.store;
    	var sortinfo = store.sortInfo;
    	
    	var rec = action.result.data;
    	if(Ext.isObject(rec) && !(rec instanceof Ext.data.Record)) {
    		rec = new store.recordType(rec);
    	}
    	
    	var selected = this.getSelections();
    	if(selected.length > 0) {
    		selected = selected[0];
    		for(var val in selected.data) {
    			selected.data[val] = rec.get(val);
    		}
    		selected.commit();
    	} else {
    		store.add(rec);
    	}
    		
    	store.sort(sortinfo.field, sortinfo.direction);
    	this.getSelectionModel().clearSelections();
    },
    
    onDeleteUser : function() {
    	var records = this.getSelections();
    	if(records.length > 0) {
    		Ext.MessageBox.confirm(
    			'Please Confirm',
    			this.msgs.deleteUserConfirm,
    			function(btn) {
    				if(btn === 'yes') {
    					this.onConfirmDeleteUser(records);
    				}
    			},
    			this
    		);
    	}
    },
    
    onConfirmDeleteUser : function(records) {
   		var msg = this.msgs.deletingUsers;
   		this.el.mask(msg, 'x-mask-loading');
   		
   		var ids = this.extractUserIds(records);
   		Ext.Ajax.request({
   			url : this.url,// + '?{' + ids.toString() + '}',
   			method : 'DELETE',
   			success : this.onDeleteUserSuccess,
   			scope : this,
   			params : {
   				userids : ids
   			}
   		});
    },
    
    onDeleteUserSuccess : function() {
    	var selected = this.getSelections();	
    	this.store.remove(selected);
    	
    	//unmask
   		this.el.unmask(); 	
    },
    
    extractUserIds : function(records) {
    	var ids = [];
    	
    	Ext.each(records, function(record) {
    		var id = record.get('user_id');
    		ids.push(id);
    	}
    	)
    	return ids;
    },
    
    getSelections : function() {
    	return this.selModel.getSelections();
    }
});

Ext.reg('sectorusersgridpanel', SectorUser.SectorUsersGridPanel);
