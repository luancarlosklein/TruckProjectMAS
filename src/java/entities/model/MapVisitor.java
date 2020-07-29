package entities.model;

/**
 * Interface used by design pattern Visitor
 */

public interface MapVisitor 
{
	public void visit(MapPlacing map);
	public void visit(MapRouting map);
}
