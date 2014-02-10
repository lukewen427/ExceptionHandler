package uk.ac.ncl.cs.esc.partitiontool;

import java.util.Iterator;
import java.util.Set;

public class DataBlockSet {

Set<DataBlock> dataBlockSet;
public DataBlockSet(Set<DataBlock> datablockset){
	this.dataBlockSet=datablockset;
 }

public DataBlock getDataBlock(String source,String Destination){
	DataBlock theDataBlock = null;
	Iterator<DataBlock>getDataBlocks=dataBlockSet.iterator();
	while(getDataBlocks.hasNext()){
		DataBlock datablock=getDataBlocks.next();
		if((source.equals(datablock.getsourceblockId()))&&(Destination.equals(datablock.destinationblockId))){
			theDataBlock=datablock;
			break;
		}
	}
	return theDataBlock;
}
}
