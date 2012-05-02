Ext.ux.Portlet = Ext.extend(Ext.Panel, {
    anchor: '100%',
    frame:true,
    collapsible:true,
    draggable:true,
    cls:'x-portlet'
    //, autoWidth: true,
    //width: 'auto'
});
Ext.reg('portlet', Ext.ux.Portlet);