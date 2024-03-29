/**
 *
 * SqlArray.java
 *
 * -----------------------------------------------------------
 *
 * Main developer cwang
 *
 */

package com.verycloud.model;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

class SqlArray<T> implements Array{
    private List<T> data;
    private int baseType;
    private String baseTypeName = null;
    
    protected SqlArray(List<T> data, int baseType){
       this.data = data;
       this.baseType = baseType;
    }
    
    protected SqlArray(List<T> data, int baseType, String baseTypeName){
       this(data, baseType);
       this.baseTypeName = baseTypeName;
    }
    
    public static class BOOLEAN extends SqlArray<Boolean>{
       public BOOLEAN(List<Boolean> data){
          super(data, Types.BIT);
       }
    }
    
    public static class INTEGER extends SqlArray<Integer>{
       public INTEGER(List<Integer> data){
          super(data, Types.INTEGER);
       }
    }
    
    public static class FLOAT extends SqlArray<Float>{
       public FLOAT(List<Float> data){
          super(data, Types.FLOAT);
       }
    }
    
    public static class DOUBLE extends SqlArray<Double>{
       public DOUBLE(List<Double> data){
          super(data, Types.DOUBLE);
       }
    }
    
    public static class STRING extends SqlArray<String>{
       public STRING(List<String> data){
          super(data, Types.VARCHAR, "text");
       }
    }
    
    public static class DATE extends SqlArray<Date>{
       public DATE(List<Date> data){
          super(data, Types.TIMESTAMP);
       }
    }
    
    public String getBaseTypeName(){
       if( baseTypeName != null )
          return baseTypeName;
       return SessionsManager.getSettings().getDialect().getTypeName( baseType );
    }

    public int getBaseType(){
       return baseType;
    }

    public Object getArray(){
       return data.toArray();
    }

    public Object getArray(long index, int count){
       int lastIndex = count-(int)index;
       if( lastIndex > data.size() )
          lastIndex = data.size();
       
       return data.subList((int)(index-1), lastIndex).toArray();
    }

    @Override
    public String toString(){
       StringBuilder result = new StringBuilder();
       result.append('{');
       boolean first = true;
       
       for(T t: data){
          if( first )
             first = false;
          else
             result.append( ',' );
          
          if( t == null ){
             result.append( "null" );
             continue;
          }
          
          switch( baseType ){
             case Types.BIT:
             case Types.BOOLEAN:
                result.append(((Boolean)t).booleanValue() ? "true" : "false");
                break;

             case Types.INTEGER:
             case Types.FLOAT:
             case Types.DOUBLE:
             case Types.REAL:
             case Types.NUMERIC:
             case Types.DECIMAL:
                result.append( t );
                 break;
                 
             case Types.VARCHAR:
                String s = (String)t;
                // Escape the string
                  result.append('\"');
                  for(int p=0; p < s.length(); ++p){
                      char ch = s.charAt( p );
                      if( ch == '\0' )
                          throw new IllegalArgumentException( "Zero bytes may not occur in string parameters." );
                      if( ch == '\\' || ch == '"' )
                          result.append('\\');
                      result.append(ch);
                  }
                  result.append('\"');
                  break;
                  
             case Types.TIMESTAMP:
                Date d = (Date)t;
                result.append('\'');
                appendDate(result, d);
                result.append( d );
                result.append('\'');
                break;

             default:
                throw new UnsupportedOperationException("Unsupported type "+baseType+" / "+getBaseTypeName());
          }
       }
       
       result.append('}');
       
       return result.toString();
    }
    
    private static GregorianCalendar calendar = null;
    protected void appendDate(StringBuilder sb, Date date){
       if (calendar == null)
          calendar = new GregorianCalendar();
       
       calendar.setTime( date );
       
       // Append Date
       {
          int l_year = calendar.get(Calendar.YEAR);
            // always use at least four digits for the year so very
            // early years, like 2, don't get misinterpreted
            //
            int l_yearlen = String.valueOf(l_year).length();
            for(int i = 4; i > l_yearlen; i--)
                sb.append("0");
    
            sb.append(l_year);
            sb.append('-');
            int l_month = calendar.get(Calendar.MONTH) + 1;
            if( l_month < 10 )
                sb.append('0');
            sb.append(l_month);
            sb.append('-');
            int l_day = calendar.get(Calendar.DAY_OF_MONTH);
            if (l_day < 10)
                sb.append('0');
            sb.append(l_day);
       }      

       sb.append(' ');
         
         // Append Time
         {
            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            if (hours < 10)
                sb.append('0');
            sb.append(hours);
    
            sb.append(':');
            int minutes = calendar.get(Calendar.MINUTE);
            if (minutes < 10)
                sb.append('0');
            sb.append(minutes);
    
            sb.append(':');
            int seconds = calendar.get(Calendar.SECOND);
            if (seconds < 10)
                sb.append('0');
            sb.append(seconds);
    
            if( date instanceof Timestamp ){
               // Add nanoseconds.
               // This won't work for postgresql versions < 7.2 which only want
               // a two digit fractional second.
    
               Timestamp t = (Timestamp) date;
               char[] decimalStr = {'0', '0', '0', '0', '0', '0', '0', '0', '0'};
               char[] nanoStr = Integer.toString( t.getNanos() ).toCharArray();
               System.arraycopy(nanoStr, 0, decimalStr, decimalStr.length - nanoStr.length, nanoStr.length);
               sb.append('.');
               sb.append(decimalStr, 0, 6);
            }
       }
         
         // Append Time Zone offset
         {
            //int offset = -(date.getTimezoneOffset());
               int offset = (calendar.get(Calendar.ZONE_OFFSET)+calendar.get(Calendar.DST_OFFSET)) / (60 * 1000);
            int absoff = Math.abs(offset);
            int hours = absoff / 60;
            int mins = absoff - hours * 60;
    
            sb.append((offset >= 0) ? "+" : "-");
    
            if (hours < 10)
                sb.append('0');
            sb.append(hours);
    
            if (mins < 10)
                sb.append('0');
            sb.append(mins);
         }
         
         // Append Era
         if( calendar.get(Calendar.ERA) == GregorianCalendar.BC )
             sb.append(" BC");
    }

    /**
     * @param newMap
     * @return
     * @throws SQLException
     * @see java.sql.Array#getArray(java.util.Map)
     */
    public Object getArray(Map<String, Class<?>> newMap) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param newIndex
     * @param newCount
     * @param newMap
     * @return
     * @throws SQLException
     * @see java.sql.Array#getArray(long, int, java.util.Map)
     */
    public Object getArray(long newIndex, int newCount,
            Map<String, Class<?>> newMap) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param newMap
     * @return
     * @throws SQLException
     * @see java.sql.Array#getResultSet(java.util.Map)
     */
    public ResultSet getResultSet(Map<String, Class<?>> newMap)
            throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param newIndex
     * @param newCount
     * @param newMap
     * @return
     * @throws SQLException
     * @see java.sql.Array#getResultSet(long, int, java.util.Map)
     */
    public ResultSet getResultSet(long newIndex, int newCount,
            Map<String, Class<?>> newMap) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @throws SQLException
     * @see java.sql.Array#free()
     */
    public void free() throws SQLException {
        // TODO Auto-generated method stub
        
    }

    /**
     * @return
     * @throws SQLException
     * @see java.sql.Array#getResultSet()
     */
    public ResultSet getResultSet() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param newIndex
     * @param newCount
     * @return
     * @throws SQLException
     * @see java.sql.Array#getResultSet(long, int)
     */
    public ResultSet getResultSet(long newIndex, int newCount)
            throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }
 }
