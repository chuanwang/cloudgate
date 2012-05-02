Ext.ns('SectorUser');

Ext.apply(Ext.form.VTypes, {
			password : function(val, field) {
				if (field.initialPassField) {
					var pwd = Ext.getCmp(field.initialPassField);
					return (val == pwd.getValue());
				}
				return true;
			},

			passwordText : 'The passwords entered do not match!'
});

SectorUser.SectorUserForm = Ext.extend(SectorUser.FormPanelBaseCls, {

	border : true,
	autoScroll : true,
	bodyStyle : 'background-color: #DFE8F6; padding: 10px',
	// layout : 'form',
	defaultType : 'textfield',
	labelWidth : 150,
	defaults : {
		width : 200,
		msgTarget : 'side'
	},

	msgs : {
		failedToLoad : 'Failed to load user {0}.'		
	},
	
	initComponent : function() {

	Ext.applyIf(this, {
		items : this.buildFormItems()
	});
	
	SectorUser.SectorUserForm.superclass.initComponent.call(this);

	if (this.record) {
		this.on({
			'render' : {
				scope : this,
				fn : this.loadRecordAfterRender
			}
		});
	}
	
	},

	buildFormItems : function() {
		return [{
					xtype : 'hidden',
					name : 'user_id'
				}, {
					fieldLabel : 'User Name',
					name : 'name',
					maxLength : 40,
					allowBlank : false
				}, {
					inputType : 'password',
					fieldLabel : 'Password',
					name : 'passwd',
					id : 'passwordField',
					maxLength : 20,
					minLength : 6,
					minLengthText : 'Password must be at least 6 characters long.',
					allowBlank : false
				}, {
					inputType : 'password',
					fieldLabel : 'Confirm Password',
					maxLength : 20,
					minLength : 6,
					minLengthText : 'Password must be at least 6 characters long.',
					allowBlank : false,
					vtype : 'password',
					id : 'confirmPasswordField',
					initialPassField : 'passwordField'
				}, {
					xtype : 'numberfield',
					fieldLabel : 'Quota',
					name : 'quota',
					allowBlank : true,
					allowDecimals : false,
					allowNegative : false
				}, {
					fieldLabel : 'Access Control List',
					name : 'acl',
					width : 300,
					allowBlank : true
				}, {
					fieldLabel : 'Read Permission',
					name : 'read_permission',
					width : 300,
					allowBlank : true
				}, {
					fieldLabel : 'Write Permission',
					width : 300,
					name : 'write_permission',
					allowBlank : true
				}, {
					xtype : 'checkbox',
					fieldLabel : 'Execute Permission',
					name : 'exec_permission'
				}

		]
	},
	
	loadRecordAfterRender : function() {
		var name = this.record.get('name');
		this.load({
			method : 'GET',
			url : '/resources/users/' + name,
			success : this.onLoadSuccess.createDelegate(this),
			failure : this.onLoadFailure.createDelegate(this)
		});
		
	},

	onLoadSuccess : function(form, action) {
		// set confirm passwd field with passworkd field value
		var passwd = action.result.data.passwd;
		this.getForm().findField('confirmPasswordField').setValue(passwd);
	},
	
	onLoadFailure : function() {
		var msg = String.format(this.msgs.failedToLoad, this.record.get('name'));
		Ext.MessageBox.alert('Error', msg);	
	}
})

Ext.reg('SectorUserForm', SectorUser.SectorUserForm);