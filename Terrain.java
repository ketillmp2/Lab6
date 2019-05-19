public enum Terrain{
    KITCHEN{
        private int creatureCounter = 0;
        public void count() {
            System.out.println("There are " + this.creatureCounter + " creatures in the " + this.getName());
        }
        public void increment() {
            this.creatureCounter++;
        }
        public void decrement() {
            this.creatureCounter--;
        }
        public String getName(){
            return "Kitchen";
        }
    },
    BEDROOM{
        private int creatureCounter = 0;
        public void count() {
            System.out.println("There are " + this.creatureCounter + " creatures in the " + this.getName());
        }
        public void increment() {
            this.creatureCounter++;
        }
        public void decrement() {
            this.creatureCounter--;
        }
        public String getName(){
            return "Bedroom";
        }
    },
    STREET{
        private int creatureCounter = 0;
        public void count() {
            System.out.println("There are " + this.creatureCounter + " creatures on the " + this.getName());
        }
        public void increment() {
            this.creatureCounter++;
        }
        public void decrement() {
            this.creatureCounter--;
        }
        public String getName(){
            return "Street";
        }
    },
    ROOF{
        private int creatureCounter = 0;
        public void count() {
            System.out.println("There are " + this.creatureCounter + " creatures on the " + this.getName());
        }
        public void increment() {
            this.creatureCounter++;
        }
        public void decrement() {
            this.creatureCounter--;
        }
        public String getName(){
            return "Roof";
        }
    },
    LIVING_ROOM{
        private int creatureCounter = 0;
        public void count() {
            System.out.println("There are " + this.creatureCounter + " creatures in the " + this.getName());
        }
        public void increment() {
            this.creatureCounter++;
        }
        public void decrement() {
            this.creatureCounter--;
        }
        public String getName(){
            return "Living Room";
        }
    },
    BATHROOM{
        private int creatureCounter = 0;
        public void count() {
            System.out.println("There are " + this.creatureCounter + " creatures in the " + this.getName());
        }
        public void increment() {
            this.creatureCounter++;
        }
        public void decrement() {
            this.creatureCounter--;
        }
        public String getName(){
            return "Bathroom";
        }
    },
    VOID{
        private int creatureCounter = 0;
        public void count() {
            System.out.println("There are " + this.creatureCounter + " creatures in the " + this.getName());
        }
        public void increment() {
            this.creatureCounter++;
        }
        public void decrement() {
            this.creatureCounter--;
        }
        public String getName() {
            return "Void";
        }
    };
    public abstract String getName();
    public abstract void count();
    public abstract void decrement();
    public abstract void increment();
    public void setName(String name) {
        System.out.println("You can't change Terrain's name");
    }
}