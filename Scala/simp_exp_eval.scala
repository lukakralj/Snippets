

class Node(val value: String, val leftChild: Option[Node], val rightChild: Option[Node]) {

    override def toString() : String = {
        (leftChild, rightChild) match {
            case (None, None) => value
            case (l, None) => "(" + value + l.get.toString() + ")"
            case (l, r) => "(" + l.get.toString() + " " + value + " " + r.get.toString() + ")"
        }
    }
}

// (1+2) - 5
val sampleTree = new Node("-", 
                        Some(new Node("+", 
                                Some(new Node("1", None, None)), 
                                Some(new Node("2", None, None)))), 
                        Some(new Node("5", None, None)))

// ((1+2)-5) * (42 / 6 + 3) + -3
//      -2    *       10      -3   = -23

val anotherExample = new Node(
"+",
Some(new Node(
    "*",
    Some(sampleTree),
    Some(new Node(
        "+",
        Some(new Node(
            "/",
            Some(new Node("42", None, None)),
            Some(new Node("6", None, None))
        )),
        Some(new Node("3", None, None))
    ))
)),
Some(new Node(
    "-", 
    Some(new Node("3", None, None)),
    None))
)

def evalTree(root: Node) : Int = {
    (root.leftChild, root.rightChild) match {
        case (None, None) => root.value.toInt // throws excpetion if malformed tree
        case (None, _) => throw new Exception("Invalid tree. Only right child found without the left child.")
        case (l, None) => (root.value) match {
            case "+" => evalTree(l.get)
            case "-" => - evalTree(l.get)
            case v => throw new Exception("Invalid unary oprator {" + v + "}.")
        }
        case (l, r) => (root.value) match {
            case "+" => evalTree(l.get) + evalTree(r.get)
            case "-" => evalTree(l.get) - evalTree(r.get)
            case "*" => evalTree(l.get) * evalTree(r.get)
            case "/" => evalTree(l.get) / evalTree(r.get)
            case v => throw new Exception("Invalid oprator {" + v + "}.")
        }
    }
}

evalTree(sampleTree)
evalTree(anotherExample)