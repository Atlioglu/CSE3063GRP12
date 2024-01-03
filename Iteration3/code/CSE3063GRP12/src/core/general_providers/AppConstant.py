class AppConstant:
    _instance = None


    def __new__(cls):
        if not cls._instance:
            cls._instance = super(AppConstant, cls).__new__(cls)
            cls._initialize()
        return cls._instance

    @classmethod
    def _initialize(cls):
        cls.back_menu_message = "Do you want to go back"
        cls.base_path = "/Iteration3/code/CSE3063GRP12/database/"

    @classmethod
    def getInstance(cls):
        if cls._instance is None:
            cls._instance = AppConstant()
        return cls._instance

    @classmethod
    def get_back_menu_message(cls):
        return cls.back_menu_message

    @classmethod
    def set_back_menu_message(cls, message):
        cls.back_menu_message = message

    @classmethod
    def get_base_path(cls):
        return cls.base_path