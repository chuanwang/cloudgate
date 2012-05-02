package com.verycloud.resources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;

import com.opendatagroup.sector.sectorjni.SNode;
import com.opendatagroup.sector.sectorjni.SectorJniClient;
import com.verycloud.datacollector.SysInfoUtil;

/**
 * Sector files server resource.
 */
public class FilesResource extends SectorServerResource {
	
	/**
	 * TODO Add class description here.
	 */
	private enum CmdType {
		/** */
		GET("get"), //$NON-NLS-1$
		/** */
		DELETE("delete"), //$NON-NLS-1$
		/** */
		NEWDIR("newdir"), //$NON-NLS-1$
		/** */
		UPLOAD("upload"), //$NON-NLS-1$
		/** */
		RENAME("rename"); //$NON-NLS-1$
		
		/** */
		private String type;
		
		/**
		 * @param value
		 */
		CmdType(String value) {
			type = value;
		}

		/**
		 * @return operation type.
		 */
		public String getType() {
			return type;
		}

	}
	
    /**
     * @param form 
     * @return file list.
     */
    @Post
    public Representation retrieveFiles(Form form) {
    	
        JSONArray arr = new JSONArray();
        
        String cmd = null;
        String path = null;
        
        for(Parameter p : form) {
        	if(p.getName().equalsIgnoreCase("cmd")) //$NON-NLS-1$
        		cmd = p.getValue();
        	else if(p.getName().equalsIgnoreCase("path")) //$NON-NLS-1$
        		path = p.getValue();
        		
        }
        
        if(CmdType.GET.getType().equals(cmd)) {
        	arr = cmdGetHandler(path);
        }
        	
        Representation rep = new JsonRepresentation(arr);

        return rep;

    }

    /**
     * @param path
     * @return JSonArray.
     */
    private JSONArray cmdGetHandler(String path) {
        
        SectorJniClient client = getSectorClient().getClient();
    	JSONArray array = new JSONArray();
    
    	SNode[] snodes = client.sectorList(path);
    	
    	int i = 0;
    	for(SNode node : snodes) {
    		JSONObject obj = new JSONObject();
    		try {
				obj.put("text", node.getName()); //$NON-NLS-1$
				
				if(node.isDir()) {
					obj.put("iconCls", "folder"); //$NON-NLS-1$ //$NON-NLS-2$
					obj.put("leaf", false); //$NON-NLS-1$
				} else {
					obj.put("iconCls", "file-txt"); //$NON-NLS-1$ //$NON-NLS-2$
					obj.put("leaf", true); //$NON-NLS-1$
					
					long fileSize = node.getSize();
					obj.put("size", SysInfoUtil.getFormattedSize(fileSize)); //$NON-NLS-1$
				}
				
				long lastUpdate = node.getTimestamp();
				obj.put("lastmod", SysInfoUtil.getFormattedTime(lastUpdate)); //$NON-NLS-1$
				
				array.put(i++, obj);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	return array;
    }
    
}
