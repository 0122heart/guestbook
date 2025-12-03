from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
import time
import random
import string

def generate_random_status():
    """랜덤 상태 메시지 생성"""
    messages = [
        "오늘도 화이팅!",
        "행복한 하루 되세요~",
        "커피 한 잔의 여유",
        "날씨가 좋네요",
        "열심히 살아가는 중",
        "즐거운 하루!",
        "평화로운 일상",
        "좋은 하루 보내세요",
        "힘내세요!",
        "Always be happy"
    ]
    
    # 랜덤 메시지 또는 랜덤 문자열 생성
    if random.choice([True, False]):
        return random.choice(messages)
    else:
        length = random.randint(5, 15)
        return ''.join(random.choices(string.ascii_letters + string.digits + ' ', k=length))

def create_test_account(driver, account_number):
    """테스트 계정 생성"""
    try:
        # 회원가입 페이지로 이동
        driver.get('http://localhost:3000/signup')
        time.sleep(1)
        
        login_id = f"테스트계정{account_number}"
        password = "1234"
        nickname = f"테스트유저{account_number}"
        status_message = generate_random_status()
        
        print(f"\n[{account_number}/10] 계정 생성 중...")
        print(f"  - 아이디: {login_id}")
        print(f"  - 닉네임: {nickname}")
        print(f"  - 상태메시지: {status_message}")
        
        # 아이디 입력
        login_id_input = WebDriverWait(driver, 10).until(
            EC.presence_of_element_located((By.NAME, "loginId"))
        )
        login_id_input.clear()
        login_id_input.send_keys(login_id)
        time.sleep(0.5)
        
        # 아이디 중복체크 버튼 클릭
        check_buttons = driver.find_elements(By.CLASS_NAME, "check-button")
        check_buttons[0].click()  # 첫 번째 중복체크 버튼 (아이디)
        time.sleep(1)
        
        # alert 처리
        try:
            alert = driver.switch_to.alert
            alert_text = alert.text
            print(f"  - 아이디 중복체크: {alert_text}")
            alert.accept()
            
            if "이미 사용" in alert_text:
                print(f"  [WARNING] 아이디가 이미 존재합니다. 건너뜁니다.")
                return False
        except:
            pass
        
        time.sleep(0.5)
        
        # 비밀번호 입력
        password_input = driver.find_element(By.NAME, "password")
        password_input.clear()
        password_input.send_keys(password)
        time.sleep(0.3)
        
        # 비밀번호 확인 입력
        password_confirm_input = driver.find_element(By.NAME, "passwordConfirm")
        password_confirm_input.clear()
        password_confirm_input.send_keys(password)
        time.sleep(0.3)
        
        # 닉네임 입력
        nickname_input = driver.find_element(By.NAME, "nickname")
        nickname_input.clear()
        nickname_input.send_keys(nickname)
        time.sleep(0.5)
        
        # 닉네임 중복체크 버튼 클릭
        check_buttons = driver.find_elements(By.CLASS_NAME, "check-button")
        check_buttons[1].click()  # 두 번째 중복체크 버튼 (닉네임)
        time.sleep(1)
        
        # alert 처리
        try:
            alert = driver.switch_to.alert
            alert_text = alert.text
            print(f"  - 닉네임 중복체크: {alert_text}")
            alert.accept()
            
            if "이미 사용" in alert_text:
                print(f"  [WARNING] 닉네임이 이미 존재합니다. 건너뜁니다.")
                return False
        except:
            pass
        
        time.sleep(0.5)
        
        # 상태메시지 입력
        status_input = driver.find_element(By.NAME, "statusMessage")
        status_input.clear()
        status_input.send_keys(status_message)
        time.sleep(0.3)
        
        # 회원가입 버튼 클릭
        submit_button = driver.find_element(By.CLASS_NAME, "submit-button")
        submit_button.click()
        time.sleep(1.5)
        
        # 회원가입 완료 alert 처리
        try:
            alert = driver.switch_to.alert
            alert_text = alert.text
            print(f"  - 결과: {alert_text}")
            alert.accept()
            
            if "완료" in alert_text:
                print(f"  [SUCCESS] 계정 생성 성공!")
                time.sleep(1)
                return True
            else:
                print(f"  [FAILED] 계정 생성 실패")
                return False
        except Exception as e:
            print(f"  [ERROR] 에러 발생: {str(e)}")
            return False
            
    except Exception as e:
        print(f"  [ERROR] 에러 발생: {str(e)}")
        return False

def main():
    # Chrome 옵션 설정
    chrome_options = Options()
    chrome_options.add_argument('--headless')  # 헤드리스 모드 활성화
    chrome_options.add_argument('--no-sandbox')
    chrome_options.add_argument('--disable-dev-shm-usage')
    chrome_options.add_argument('--disable-gpu')
    chrome_options.add_argument('--window-size=1920,1080')
    chrome_options.add_argument('--disable-blink-features=AutomationControlled')  # 자동화 감지 방지
    
    # WebDriver 초기화
    driver = webdriver.Chrome(options=chrome_options)
    
    try:
        print("=" * 60)
        print("테스트 계정 자동 생성 시작 (헤드리스 모드)")
        print("=" * 60)
        
        success_count = 0
        fail_count = 0
        
        # 10개 계정 생성
        for i in range(1, 11):
            result = create_test_account(driver, i)
            
            if result:
                success_count += 1
            else:
                fail_count += 1
            
            time.sleep(1)  # 다음 계정 생성 전 대기
        
        print("\n" + "=" * 60)
        print("생성 결과")
        print("=" * 60)
        print(f"성공: {success_count}개")
        print(f"실패: {fail_count}개")
        print(f"총 {success_count + fail_count}개 계정 처리 완료")
        print("=" * 60)
        
    except Exception as e:
        print(f"\n[ERROR] 전체 프로세스 에러: {str(e)}")
    
    finally:
        print("\n브라우저 종료 중...")
        driver.quit()
        print("완료!")

if __name__ == "__main__":
    main()