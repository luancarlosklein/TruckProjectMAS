package entities.model;

/**
 * Interface used by design pattern Visitor
 */

public interface GridVisitor 
{
	public void visit(GridLayout layout);
	public void visit(GridRoutes routes);
}