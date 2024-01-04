class MenuView:
    def show_menu_list(self, menu_items):
        for index, item in enumerate(menu_items, start=1):
            print(f"{index}. {item}")

    def show_error_message(self, exception):
        print(f"Error: {str(exception)}")