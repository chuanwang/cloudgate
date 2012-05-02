Ext.ns('SectorUser');

SectorUser.SectorUserWindow = Ext.extend(Ext.Window, {

	layout : 'fit',
	modal : true,
	resizable : false,
	draggable : false,
	center : true,
	closeable : false,
	height : 300,
	width : 600,
	
	msgs : {
		errorsInForm : 'There are errors in the form. Please correct and try again.',
		saving : 'Saving user {0}'
	},
	
	initComponent : function() {
		
		Ext.applyIf(this, {
			title : 'Sector User',
			iconCls : (this.record) ? 'icon-user-edit' : 'icon-user-add',
			items : this.buildFormPanel(),
			buttons : this.buildButtons()
		})
		
		this.addEvents({
			usersaved : true
		})
		
		SectorUser.SectorUserWindow.superclass.initComponent.call(this);
		
	},
	
	buildFormPanel : function() {
		return [{
			xtype : 'SectorUserForm',
			itemId : 'SectorUserForm',
			record : this.record,
			border : false
		}]
	},
	
	buildButtons : function() {
		return [
		{
			text : 'Cancel',
			iconCls : 'icon-cross',
			scope : this,
			handler : this.onCancelBtn
		},
		{
			text : 'Save',
			iconCls : 'icon-save',
			scope :  this,
			handler : this.onSaveBtn
		}
		]
	},
	
	onCancelBtn : function() {
		this.close();
	},
	
	onSaveBtn : function() {
		var formPanel = this.getComponent('SectorUserForm');
		
		if(formPanel.getForm().isValid()) {
			var values = formPanel.getValues();	
			var msg = String.format(this.msgs.saving, values.name);
			
			this.el.mask(msg, 'x-mask-loading');
			
			if (this.record) {
				formPanel.getForm().submit({
							method : 'PUT',
							url : '/resources/users/' + values.name,
							scope : this,
							success : this.onSubmitSuccess,
							failure : this.onSubmitFailure
				});

			} else {
				formPanel.getForm().submit({
							method : 'POST',
							url : '/resources/users',
							scope : this,
							success : this.onSubmitSuccess,
							failure : this.onSubmitFailure
				});
				
			}
		} else {
			Ext.MessageBox.alert(
				'Error',
				this.msgs.errorsInForm
			);
		}
	},
	
	onSubmitSuccess : function(form, action) {
		var success = action.result.success;
		this.fireEvent('sectorusersaved', action);
		this.close();
	},
	
	onSubmitFailure : function(form, action) {
		var success = action.result.success;
		var msg = action.result.msg;
		if(!success) {
			this.el.unmask();
			Ext.MessageBox.alert(
				'Error',
				msg
			);
		}
		
	}
})