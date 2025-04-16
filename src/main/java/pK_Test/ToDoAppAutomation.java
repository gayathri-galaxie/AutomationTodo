package pK_Test;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ToDoAppAutomation {

	public static void main(String[] args) {

// To Open ChromeDriver
		WebDriver driver = new ChromeDriver();

// Maximize the Window
		driver.manage().window().maximize();

        driver.manage().deleteAllCookies();
        
		try {
        // Navigate to the To-do WebPage
		driver.get("https://todomvc.com/examples/react/dist/");
		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		String title = driver.getTitle();
		System.out.println("Title: " + title);
		boolean isTitleReflected = title.contains("TodoMVC: React");
		if(isTitleReflected) {
			System.out.println("PASS");
		}else {
			System.out.println("FAIL");
		}

//TC01,TC02,TC03
        //Adding New To-Dos and Validating
		WebElement inputField = driver.findElement(By.className("new-todo"));
		inputField.sendKeys("Buy Milk");
		inputField.sendKeys("\n");

		// Check if input field is cleared
        if (!inputField.getAttribute("value").isEmpty()) {
            throw new AssertionError("TC01,TC02,TC03-FAIL- Input Field Not Cleared");
        }
         System.out.println("TC01,TC02,TC03-PASS- Input Field Cleared");

         // Verify the new To-do appears in the list
         WebElement todoOne = driver.findElement(By.className("todo-list"));
         
         if (!todoOne.getText().equals("Buy Milk")) {
             throw new AssertionError("TC01,TC02,TC03-FAIL:Item NOT found.");
         }
          System.out.println("TC01,TC02,TC03-PASS- To-do item added. "+todoOne.getText());

//TC04,TC06
          //Adding Multiple To-Dos
          inputField = driver.findElement(By.className("new-todo"));
          inputField.sendKeys("Exercise");
          inputField.sendKeys("\n");
          
          WebElement todoFour1 = driver.findElement(By.xpath("//ul[@class='todo-list']/li[1]/div/label"));
          WebElement todoFour2 = driver.findElement(By.xpath("//ul[@class='todo-list']/li[2]/div/label"));

          if (!todoFour1.getText().equals("Buy Milk")) {
              throw new AssertionError("TC04,TC06-FAIL- Second To-do item NOT Found.");
          }
          if (!todoFour2.getText().equals("Exercise")){
              throw new AssertionError("TC04,TC06-FAIL- First To-do item NOT found after adding Second To-do.");
          }

          System.out.println("TC04,TC06-PASS- Both Todo items added. "+todoFour1.getText()+" & "+todoFour2.getText());

//TC05
          //Verify Empty(Only Spaces) To-Dos are not added.
     try{
          inputField = driver.findElement(By.className("new-todo"));
          inputField.sendKeys("   ");
          inputField.sendKeys("\n");

          // Verify no new item is added to the list
          String inputValue = inputField.getAttribute("value");
          if (inputValue == null || inputValue.isEmpty()) {
              System.out.println("TC05 - PASS - Input Field Cleared["+inputField.getAttribute("value")+"]");
          } else {
              throw new AssertionError("TC05 - FAIL - Input Field Not Cleared["+inputField.getAttribute("value")+"]");
          }
      } catch (AssertionError e){
        	    System.err.println(e.getMessage());
        }	    

//TC07
          //Verify that each todo item has a checkbox to mark it as completed.    
          WebElement todoItem1 = driver.findElement(By.xpath("//ul[@class='todo-list']/li[1]"));
          WebElement todoItem2 = driver.findElement(By.xpath("//ul[@class='todo-list']/li[2]"));          
          WebElement checkBox1 = todoItem1.findElement(By.className("toggle"));
          WebElement checkBox2 = todoItem2.findElement(By.className("toggle"));
          if (checkBox1 == null && checkBox2 == null) {
              throw new AssertionError("TC07-FAIL- Checkbox is NOT present");
          }
          System.out.println("TC07-PASS- Checkbox is present for all items.");
//TC08
          //Verify that the count of items updated after addition.
          WebElement itemsLeftCounter = driver.findElement(By.className("todo-count"));
          
          String initialCountText = itemsLeftCounter.getText();
          int initialCount = Integer.parseInt(initialCountText.split(" ")[0]);

          inputField.sendKeys("Walk the dog");
          inputField.sendKeys("\n");

          String currentCountText = itemsLeftCounter.getText();
          int currentCount = Integer.parseInt(currentCountText.split(" ")[0]);

          if (currentCount != initialCount + 1) {
              throw new AssertionError("TC08-FAIL- Item count did NOT change correctly.");
          }
          System.out.println("TC08-PASS- Item count Changes correctly: "+itemsLeftCounter.getText());
          
//TC20
          //Verify that clicking the delete button removes the corresponding todo item from the list.
          WebElement todoCountElement = driver.findElement(By.className("todo-count"));
          String beforeDeleteCountText = todoCountElement.getText();
          int beforeDeleteCount = Integer.parseInt(beforeDeleteCountText.split(" ")[0]);
          
          WebElement todoItemToDelete = driver.findElement(By.xpath("//ul[@class='todo-list']/li[1]"));
          WebElement deleteButton = todoItemToDelete.findElement(By.className("destroy"));

//TC19         
          // Verify that each todo item has a "delete" button (e.g., an "X" icon).
          Actions actionsHover = new Actions(driver);
          actionsHover.moveToElement(todoItemToDelete).perform();
          
          if (!deleteButton.isDisplayed()) {
              throw new AssertionError("TC19-FAIL - Delete button is NOT visible.");
          }
          System.out.println("TC19-PASS- Delete button is visible.");
          
          
          actionsHover.click(deleteButton).perform();

          WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
          wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//ul[@class='todo-list']/li"), beforeDeleteCount - 1));
          if (ExpectedConditions.numberOfElementsToBe(By.xpath("//ul[@class='todo-list']/li"), 0) != null) {
              System.out.println("TC20-PASS- Todo item was deleted.");
          } else {
        	  System.out.println("TC20-FAIL- Todo item NOT deleted.");
          }
//TC21        
          // Verify that the count of active todos is updated correctly after deleting a todo.
          String afterDeleteCountText = todoCountElement.getText();
          int afterDeleteCount = Integer.parseInt(afterDeleteCountText.split(" ")[0]);

          if (afterDeleteCount != beforeDeleteCount - 1) {
              throw new AssertionError("TC21-FAIL- Item count was NOT updated. Expected: " + (beforeDeleteCount - 1) + ", but got: " + afterDeleteCount);
          }

          System.out.println("TC21-PASS- Item count was Updated correctly. Initial count: " + beforeDeleteCount + ", New count: " + afterDeleteCount);

                                       
	}catch (AssertionError e) {
            System.err.println(e.getMessage());
        } 
	//test a		
	}
}
		  
            

