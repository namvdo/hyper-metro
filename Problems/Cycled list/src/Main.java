import java.io.*;
import java.util.*;


class Main {

    static class Node<E> {

        private E value;
        private Node<E> next;
        private Node<E> prev;

        Node(E element, Node<E> next, Node<E> prev) {
            this.value = element;
            this.next = next;
            this.prev = prev;
        }

        Node<E> getNext() {
            return next;
        }

        Node<E> getPrev() {
            return prev;
        }
    }

    public class DoublyLinkedList<E> {

        public Node head;
        public Node tail;
        public int size;

        DoublyLinkedList() {
            size = 0;
        }

        public Node<E> getHead() {
            return head;
        }

        public Node<E> getTail() {
            return tail;
        }

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public String toString() {

            Node<E> tmp = head;
            StringBuilder result = new StringBuilder();

            while (tmp != null) {
                result.append(tmp.value).append(" ");
                tmp = tmp.next;
            }

            return result.toString();
        }

        void addFirst(E elem) {

            Node<E> tmp = new Node<>(elem, head, null);

            if (head != null) {
                head.prev = tmp;
            }

            head = tmp;

            if (tail == null) {
                tail = tmp;
            }
            size++;
            
        }

        void addLast(E elem) {

            Node<E> tmp = new Node<>(elem, null, tail);

            if (tail != null) {
                tail.next = tmp;
            }

            tail = tmp;

            if (head == null) {
                head = tmp;
            }
            size++;
        }

        void add(E elem, Node<E> curr) {

            if (curr == null) {
                throw new NoSuchElementException();
            }

            Node<E> tmp = new Node<>(elem, curr, null);

            if (curr.prev != null) {
                curr.prev.next = tmp;
                tmp.prev = curr.prev;
                curr.prev = tmp;
            } else {
                curr.prev = tmp;
                tmp.next = curr;
                head = tmp;
            }
            size++;
        }

        public void removeFirst() {
            if (size == 0) {
                throw new NoSuchElementException();
            }
            head = head.next;
            head.prev = null;
            size--;
        }

        public void removeLast() {
            if (size == 0) {
                throw new NoSuchElementException();
            }
            tail = tail.prev;
            tail.next = null;
            size--;
        }

        public void remove(Node<E> curr) {
            if (curr == null)
                throw new NoSuchElementException();
            if (curr.prev == null) {
                removeFirst();
                return;
            }
            if (curr.next == null) {
                removeLast();
                return;
            }
            curr.prev.next = curr.next;
            curr.next.prev = curr.prev;
            size--;
        }

    }

    public static void main(String[] args) {
     Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        int n = Integer.parseInt(line.split(" ")[0]);
        int cmds = Integer.parseInt(line.split(" ")[1]);
        List<Integer> list = new LinkedList<>();
        String input = sc.nextLine();
        for(int i = 0; i < n; i++) {
            list.add(Integer.parseInt(input.split(" ")[i]));
        }

        String[] xxx = new String[cmds];
        for(int i = 0; i < xxx.length; i++) {
            xxx[i] = sc.nextLine();
        }
        int currentPos = 0;
        for(int i = 0; i < cmds; i++) {
            String cmd = xxx[i].split(" ")[0];
            int value = Integer.parseInt(xxx[i].split(" ")[1]);
            if (cmd.equals("r")) { // 1 3 4 5 6 7 8
                list.remove((currentPos + value) % list.size());
                if ((currentPos + value) % list.size() > currentPos) {
                    currentPos += 1;
                    if (currentPos == list.size()) {
                        currentPos = 0;
                    }
                }

            } else { // 1 2 3* 4 5 6* 7 8 (5)
                if (currentPos - value >= 0) {
                    list.remove(currentPos - value);
                } else {
                    list.remove(Math.abs(list.size() - (Math.abs(currentPos - value))) % list.size());
                }
                if (Math.abs(currentPos - value)  < currentPos) {
                    currentPos -= 2;
                    if (currentPos < 0) {
                        currentPos = list.size() - 1;
                    }
                } else {
                    currentPos -= 1;
                }
            }
        }
        for (int x: list
             ) {
            System.out.print(x + " ");
        }
         
    }
}
