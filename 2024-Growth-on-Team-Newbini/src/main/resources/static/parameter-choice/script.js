let selectedProblemTypes = [];
let selectedDifficulty = "";

function toggleProblemType(button) {
    const problemType = button.innerText;
    if (selectedProblemTypes.includes(problemType)) {
        selectedProblemTypes = selectedProblemTypes.filter(type => type !== problemType);
        button.classList.remove('selected');
    } else {
        selectedProblemTypes.push(problemType);
        button.classList.add('selected');
    }
}

function selectDifficulty(button) {
    const difficultyButtons = document.querySelectorAll('.difficulty button');
    difficultyButtons.forEach(btn => btn.classList.remove('selected'));
    button.classList.add('selected');
    selectedDifficulty = button.innerText;
}

function submitForm() {
    document.getElementById('problem_types').value = selectedProblemTypes.join(',');
    document.getElementById('difficulty').value = selectedDifficulty;
    document.getElementById('quizForm').submit();
}