public class Main {

    public static void main(String[] args) {
        MyContainer<String> container = new MyContainer<>();

        container.add("Hello");
        container.add("World");
        container.add("!");

        System.out.println("После add: " + container);
        System.out.println("Размер контейнера: " + container.size());

        System.out.println("Элемент по индексу 1: " + container.get(1));
        container.set(1, "Java");
        System.out.println("После set(1, \"Java\"): " + container);

        container.insert(1, "persik");
        System.out.println("После insert(1, \"persik\"): " + container);

        System.out.println("Содержит \"World\"? " + container.contains("World"));
        System.out.println("Индекс \"Java\": " + container.indexOf("Java"));

        container.remove("persik");
        System.out.println("После remove(\"persik\"): " + container);

        container.remove(0);
        System.out.println("После remove(0): " + container);

        container.add("I");
        container.add("love");
        container.add("AMM");
        System.out.println("Перед reverse(): " + container);
        container.reverse();
        System.out.println("После reverse(): " + container);

        container.swap(0, container.size() - 1);
        System.out.println("После swap(0, size-1): " + container);

        System.out.println("\nПеребор через итератор:");
        MyIterator<String> iterator = container.iterator();
        while (iterator.hasNext()) {
            System.out.println("  * " + iterator.next());
        }

        System.out.println("\nПеребор через forEach:");
        container.forEach(item -> System.out.println("  -> " + item));

        Object[] copy = container.toArray();
        System.out.println("\nКопия массива:");
        for (Object obj : copy) {
            System.out.println("  [copy] " + obj);
        }

        container.clear();
        System.out.println("\nПосле clear(): " + container);
        System.out.println("Пустой? " + container.isEmpty());
    }

    public interface MyIterator<T> {
        boolean hasNext();
        T next();
    }

    public interface ElementProcessor<T> {
        void process(T element);
    }

    public static class MyContainer<T> {
        private static final int DEFAULT_CAPACITY = 10;

        private Object[] elements;
        private int size;

        public MyContainer() {
            this(DEFAULT_CAPACITY);
        }

        public MyContainer(int initialCapacity) {
            if (initialCapacity <= 0) {
                throw new IllegalArgumentException("Вместимость должна быть > 0");
            }
            elements = new Object[initialCapacity];
            size = 0;
        }

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public void add(T element) {
            ensureCapacity(size + 1);
            elements[size] = element;
            size++;
        }

        public void insert(int index, T element) {
            checkPositionIndex(index);
            ensureCapacity(size + 1);

            for (int i = size; i > index; i--) {
                elements[i] = elements[i - 1];
            }
            elements[index] = element;
            size++;
        }

        @SuppressWarnings("unchecked")
        public T get(int index) {
            checkElementIndex(index);
            return (T) elements[index];
        }

        @SuppressWarnings("unchecked")
        public void set(int index, T element) {
            checkElementIndex(index);
            T oldValue = (T) elements[index];
            elements[index] = element;
        }

        @SuppressWarnings("unchecked")
        public void remove(int index) {
            checkElementIndex(index);
            T removed = (T) elements[index];

            for (int i = index; i < size - 1; i++) {
                elements[i] = elements[i + 1];
            }

            elements[size - 1] = null;
            size--;
        }

        public void remove(T value) {
            int index = indexOf(value);
            if (index >= 0) {
                remove(index);
            }
        }

        public boolean contains(T value) {
            return indexOf(value) >= 0;
        }

        public int indexOf(T value) {
            if (value == null) {
                for (int i = 0; i < size; i++) {
                    if (elements[i] == null) {
                        return i;
                    }
                }
            } else {
                for (int i = 0; i < size; i++) {
                    if (value.equals(elements[i])) {
                        return i;
                    }
                }
            }
            return -1;
        }

        public void clear() {
            for (int i = 0; i < size; i++) {
                elements[i] = null;
            }
            size = 0;
        }

        public void swap(int indexA, int indexB) {
            checkElementIndex(indexA);
            checkElementIndex(indexB);
            if (indexA == indexB) {
                return;
            }
            Object temp = elements[indexA];
            elements[indexA] = elements[indexB];
            elements[indexB] = temp;
        }

        public void reverse() {
            int mid = size / 2;
            for (int left = 0; left < mid; left++) {
                int right = size - 1 - left;
                swap(left, right);
            }
        }

        public Object[] toArray() {
            Object[] copy = new Object[size];
            for (int i = 0; i < size; i++) {
                copy[i] = elements[i];
            }
            return copy;
        }

        public MyIterator<T> iterator() {
            return new ContainerIterator();
        }

        public void forEach(ElementProcessor<T> processor) {
            for (int i = 0; i < size; i++) {
                @SuppressWarnings("unchecked")
                T value = (T) elements[i];
                processor.process(value);
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("MyContainer{size=").append(size).append(", elements=[");

            for (int i = 0; i < size; i++) {
                sb.append(elements[i]);
                if (i < size - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]}");
            return sb.toString();
        }

        private void ensureCapacity(int minCapacity) {
            int oldCapacity = elements.length;
            if (minCapacity <= oldCapacity) {
                return;
            }

            int newCapacity = oldCapacity + oldCapacity / 2 + 1;
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }

            Object[] newArray = new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newArray[i] = elements[i];
            }

            elements = newArray;
        }

        private void checkElementIndex(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException(
                        "Неверный индекс: " + index + ", допустимый диапазон: 0.." + (size - 1)
                );
            }
        }

        private void checkPositionIndex(int index) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException(
                        "Неверная позиция: " + index + ", допустимый диапазон: 0.." + size
                );
            }
        }

        private class ContainerIterator implements MyIterator<T> {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @SuppressWarnings("unchecked")
            @Override
            public T next() {
                if (!hasNext()) {
                    throw new IllegalStateException("Элементы закончились");
                }
                T value = (T) elements[cursor];
                cursor++;
                return value;
            }
        }
    }
}