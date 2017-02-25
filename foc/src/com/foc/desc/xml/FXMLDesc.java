package com.foc.desc.xml;

public interface FXMLDesc {
  public static final String TAG_TABLE  = "Table";
  public static final String TAG_INDEX  = "Index";
  public static final String TAG_JOINS  = "Joins";
  
  //Filter Tags and Attributes
  public static final String TAG_FILTER                  = "Filter";
  public static final String TAG_FILTER_CONDITION        = "FilterCondition";
  public static final String ATT_FILTER_ON_TABLE         = "onTable";
  public static final String ATT_FILTER_ON_FIELD         = "field";
  public static final String ATT_FILTER_CONDITION_PREFIX = "prefix";
  
  //Fields Predefined
  public static final String TAG_REF           = "REF";
  public static final String TAG_CODE          = "CODE";
  public static final String TAG_EXTERNAL_CODE = "EXTERNAL_CODE";
  public static final String TAG_DATE          = "DATE";
  public static final String TAG_DESCRIPTION   = "DESCRIPTION";
  public static final String TAG_NAME          = "NAME";
  public static final String TAG_ORDER         = "ORDER";
  public static final String TAG_NOT_COMPLETED = "NOT_COMPLETED";
  public static final String TAG_IS_SYSTEM     = "IS_SYSTEM";
  public static final String TAG_TREE          = "TREE";
  
  //Field Types
  public static final String TAG_STRING          = "String";
  public static final String TAG_INTEGER         = "Integer";
  public static final String TAG_BOOLEAN         = "Boolean";
  public static final String TAG_DOUBLE          = "Double";
  public static final String TAG_DATE_FIELD      = "Date";
  public static final String TAG_TIME_FIELD      = "Time";
  public static final String TAG_MULTIPLE_CHOICE = "MultipleChoice";
  public static final String TAG_MULTIPLE_CHOICE_STRING = "MultipleChoiceString";
  public static final String TAG_BLOB            = "Blob";
  
  //Object field
  public static final String TAG_OBJECT           = "Object";
  public static final String ATT_CASCADE          = "cascade";
  public static final String ATT_DETACH           = "detach";
  public static final String ATT_SAVE_ONE_BY_ONE  = "oneByOneSave";
  public static final String ATT_TABLE            = "table";
  public static final String ATT_CACHED_LIST      = "cachedList";
  public static final String ATT_FORCED_DB_NAME   = "forcedDBName";
  public static final String ATT_NULL_VALUES_ALLOWED = "nullValueAllowed";
  
  //TAG_MULTIPLE_CHOICE
  public static final String ATT_SORT_ITEMS      = "sortChoices";
  public static final String TAG_CHOICE          = "Choice";
  public static final String ATT_ID              = "id";
  public static final String ATT_CAPTION         = "caption";
  
  //Attributes for MultipleChoice String
  public static final String ATT_SAME_COLUMN = "sameCol";
  
  public static final String ATT_NAME        = "name";
  public static final String ATT_TITLE       = "title";
  public static final String ATT_SIZE        = "size";
  public static final String ATT_DECIMALS    = "decimals";
  public static final String ATT_KEY         = "partOfKey";
  public static final String ATT_MANDATORY   = "mandatory";
  public static final String ATT_GROUPING    = "grouping";
  
  //Table node attributes 
  public static final String ATT_WORKFLOW    = "workflow";
  public static final String ATT_TREE        = "tree";
  public static final String ATT_DB_RESIDENT = "dbResident";
  public static final String ATT_CACHED      = "cached";
  public static final String ATT_DB_SOURCE   = "dbSource";
  public static final String ATT_ALLOW_ADAPT_DATA_MODEL = "allowAdaptDataModel";
  public static final String ATT_IN_TABLE_EDITABLE = "inTableEditable";
  public static final String ATT_STORAGE_NAME     = "storageName";

  //Join
  //-------------------------
  public static final String TAG_JOIN  = "Join";
  
  public static final String ATT_JOIN_ALIAS = "alias";
  public static final String ATT_JOIN_TABLE = "table";
  public static final String ATT_JOIN_ON    = "on";
  public static final String ATT_JOIN_TYPE  = "type";
  
  public static final String TAG_JOIN_FIELD   = "JoinField";
  public static final String ATT_JOIN_FLD_SRC = "sourceField";
  public static final String ATT_JOIN_FLD_TAR = "targetField";
  //-------------------------
  
}
