package loans.status;


public enum StatusENUM implements Status{

    PENDING("pending"){

    },

    ACTIVE("active"){

    },

    RISK("risk"){

    },
    
    FINISHED("finished"){

    },

    NEW("new"){

    };

    private String name;

    StatusENUM(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
