document.getElementById('submit-button').addEventListener('click', function() {

    const questions = document.querySelectorAll('.question');
    let answers = [];
    quiz_questions.forEach(function(question) {
        answers.push(question.answer);
    });

    let score = 0;
    questions.forEach((question, index) => {
    const type = question.querySelector('div').className;
    const answer = answers[index];

    console.log(type);
    console.log(answer);

    switch (type) {
        case 'multiple':
            var selectedOption = question.querySelector('input[type="radio"]:checked');
            console.log(selectedOption);
            if (selectedOption) {
                const userAnswer = selectedOption.value;
                console.log(userAnswer);
                if (userAnswer === answer) {
                    score++;
                    console.log(score);
                    question.classList.add('correct');
                } else {
                    question.classList.add('incorrect');
                }
            }
            else {
                question.classList.add('unsolved');
            }
            break;
        case 'essay':
            const userAnswer = question.querySelector('input[type="text"]').value;
            if (userAnswer) {
                if (userAnswer.trim().toLowerCase() === answer.trim().toLowerCase()) {
                    score++;
                    question.classList.add('correct');
                } else {
                    question.classList.add('incorrect');
                }
            } else {
                question.classList.add('unsolved');
            }
            break;
        case 'OX':
            var selectedOption = question.querySelector('input[type="radio"]:checked');
            if (selectedOption) {
                const userAnswer = selectedOption.value;
                if (userAnswer === answer) {
                score++;
                question.classList.add('correct');
                } else {
                    question.classList.add('incorrect');
                }
            }
            else {
                question.classList.add('unsolved');
            }
            break;
    }

    const button = document.createElement('button');
    button.textContent = '북마크';
    button.classList.add('bookmark-btn');

    button.addEventListener('click', function() {
        alert('북마크 되었습니다.');
    });

    question.appendChild(button);
    });

    document.querySelectorAll('.answer').forEach(function(element) {
        element.style.display = 'flex';
    });

    document.querySelectorAll('.short-answer').forEach(function(element) {
        element.disabled = true;
    });



});